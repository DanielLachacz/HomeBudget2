package com.example.daniellachacz.homebudget;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.Map;
import java.util.Objects;


public class BudgetFragment extends Fragment {


    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    private static final String TAG = "ListFragment";

    private TextView mBudgetText;
    private TextView mBudgetText2;
    private TextView mBudgetText3;

    public BudgetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_budget, container, false);

        mBudgetText = v.findViewById(R.id.budgetText);
        mBudgetText2 = v.findViewById(R.id.budgetText2);
        mBudgetText3 = v.findViewById(R.id.budgetText3);

        final PieChart mPieChart = (PieChart) v.findViewById(R.id.piechart);

        mRef = FirebaseDatabase.getInstance().getReference().child("Expense");
        mAuth = FirebaseAuth.getInstance();
        
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (null != user) {

                    onSignedInInitialize(user);
                    Log.d(TAG, "onAuthStateChanged: signed_in: " + user.getUid());
                } else {

                    Log.d(TAG, "onAuthStateChanged: signed_out: ");

                }
            }
        };
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mAuth = mRef.child(uid);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double totalIncome = 0.0;
                for (DataSnapshot ds : dataSnapshot.child("Income").getChildren()) {
                    totalIncome += Double.parseDouble(String.valueOf(ds.child("incomeValue").getValue()));
                }
                mBudgetText.setText((String.valueOf(totalIncome)));
                Log.d("TAG", String.valueOf(totalIncome));

                double totalExpense = 0.0;
                for (DataSnapshot ds2 : dataSnapshot.child("Expense").getChildren()) {
                    totalExpense += Double.parseDouble(String.valueOf(ds2.child("value").getValue()));
                }
                mBudgetText2.setText(String.valueOf("- " + totalExpense));
                Log.d("TAG", String.valueOf(totalExpense));

                mBudgetText3.setText(String.valueOf(totalIncome - totalExpense));

                mPieChart.addPieSlice(new PieModel((float) totalIncome, Color.parseColor("#22af15")));
                mPieChart.addPieSlice(new PieModel((float) totalExpense, Color.parseColor("#ab0808")));
                mPieChart.startAnimation();

                /*
                    Copyright (C) 2015 Paul Cech

                  Licensed under the Apache License, Version 2.0 (the "License");
                  you may not use this file except in compliance with the License.
                  You may obtain a copy of the License at

                  http://www.apache.org/licenses/LICENSE-2.0

                  Unless required by applicable law or agreed to in writing, software
                  distributed under the License is distributed on an "AS IS" BASIS,
                  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
                  See the License for the specific language governing permissions and
                  limitations under the License.
                 */

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mAuth.addListenerForSingleValueEvent(eventListener);

        return  v;
    }

    private void onSignedInInitialize(FirebaseUser user) {
        user.reload();
        if (null != user) {
            FirebaseUser currentUser = mAuth.getCurrentUser();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }


}
