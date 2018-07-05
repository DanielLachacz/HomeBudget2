package com.example.daniellachacz.homebudget;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Objects;


public class BudgetFragment extends Fragment {


    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    private static final String TAG = "ListFragment";

    private TextView mBudgetText;
    private TextView mBudgetText2;
    private TextView mBudgetText3;
    private TextView mBudgetText4;
    private TextView mBudgetText5;
    private TextView mBudgetText6;
    private TextView mBudgetText7;
    private TextView mBudgetText8;
    private TextView mBudgetText9;
    private TextView mBudgetText10;
    private TextView mBudgetText11;
    private TextView mBudgetText12;

    static Calendar cal = Calendar.getInstance();
    int month = cal.get(Calendar.MONTH); // month...
    int week = cal.get(Calendar.WEEK_OF_YEAR);
    int day = cal.get(Calendar.DAY_OF_MONTH);

    private double incomeDay;
    private double expenseDay;
    private double incomeWeek;
    private double expenseWeek;
    private double incomeMonth;
    private double expenseMonth;

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
        mBudgetText4 = v.findViewById(R.id.budgetText4);
        mBudgetText5 = v.findViewById(R.id.budgetText5);
        mBudgetText6 = v.findViewById(R.id.budgetText6);
        mBudgetText7 = v.findViewById(R.id.budgetText7);
        mBudgetText8 = v.findViewById(R.id.budgetText8);
        mBudgetText9 = v.findViewById(R.id.budgetText9);
        mBudgetText10 = v.findViewById(R.id.budgetText10);
        mBudgetText11 = v.findViewById(R.id.budgetText11);
        mBudgetText12 = v.findViewById(R.id.budgetText12);

        final PieChart mPieChart = (PieChart) v.findViewById(R.id.piechart);

        mRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        
        final String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (null != user) {

                    onSignedInInitialize(user);
                    Log.d(TAG, "onAuthStateChanged: Signed_In: " + user.getUid());
                } else {

                    Log.d(TAG, "onAuthStateChanged: Signed_Out: ");

                }
            }
        };


        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();                      // Method for total Expenses and Incomes (Entire database)
        DatabaseReference mAuth = mRef.child(uid);                                                   // and sum
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double totalIncome = 0.0;
                for (DataSnapshot ds : dataSnapshot.child("Income").getChildren()) {
                    totalIncome += Double.parseDouble(String.valueOf(ds.child("incomeValue").getValue()));
                }
                mBudgetText.setText((String.valueOf(totalIncome)));
                Log.d("TAG","totalIncome " + String.valueOf(totalIncome));

                double totalExpense = 0.0;
                for (DataSnapshot ds2 : dataSnapshot.child("Expense").getChildren()) {
                    totalExpense += Double.parseDouble(String.valueOf(ds2.child("value").getValue()));
                }
                mBudgetText2.setText(String.valueOf("- " + totalExpense));
                Log.d("TAG", "totalExpense " + String.valueOf(totalExpense));

                DecimalFormat df = new DecimalFormat("0.00");

                mBudgetText3.setText(df.format(totalIncome - totalExpense));
                if(totalIncome < totalExpense) {
                    mBudgetText3.setTextColor(Color.parseColor("#ab0808"));
                }
                else if(totalIncome > totalExpense) {
                    mBudgetText3.setTextColor(Color.parseColor("#22af15"));
                }

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
        mAuth.addValueEventListener(eventListener);


        Query query = FirebaseDatabase.getInstance().getReference().child(uid).child("Income").orderByChild("incomeDay").equalTo(day);
        query.addValueEventListener  (new ValueEventListener() {                                                                       //Method for Income per Day
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       double totalIncomeDay = 0.0;
                       for (DataSnapshot ds : dataSnapshot.getChildren()) {
                           totalIncomeDay += Double.parseDouble(String.valueOf(ds.child("incomeValue").getValue()));
                       }
                       mBudgetText4.setText(String.valueOf(totalIncomeDay));
                       Log.d("TAG", "totalIncomeDay " + String.valueOf(totalIncomeDay));

                       incomeDay += totalIncomeDay;
                        Log.d("TAG", "incomeDay " + String.valueOf(incomeDay));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        Query query2 = FirebaseDatabase.getInstance().getReference().child(uid).child("Expense").orderByChild("day").equalTo(day);
        query2.addValueEventListener(new ValueEventListener() {                                                                    //Method for Expense per Day
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double totalExpenseDay = 0.0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    totalExpenseDay += Double.parseDouble(String.valueOf(ds.child("value").getValue()));
                }
                mBudgetText5.setText(String.valueOf("- " + totalExpenseDay));
                Log.d("TAG", "totalExpenseDay " + String.valueOf(totalExpenseDay));

                expenseDay += totalExpenseDay;
                Log.d("TAG", "expenseDay " + String.valueOf(expenseDay));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


        Query query3 = FirebaseDatabase.getInstance().getReference().child(uid).child("Income").orderByChild("incomeWeek").equalTo(week);
        query3.addValueEventListener(new ValueEventListener() {                                                                           //Method for Income per Week
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double totalIncomeWeek = 0.0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    totalIncomeWeek += Double.parseDouble(String.valueOf(ds.child("incomeValue").getValue()));
                }
                mBudgetText6.setText(String.valueOf(totalIncomeWeek));
                Log.d("TAG", "totalIncomeWeek " + String.valueOf(totalIncomeWeek));

                incomeWeek += totalIncomeWeek;
                Log.d("TAG", "incomeWeek " + String.valueOf(incomeWeek));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Query query4 = FirebaseDatabase.getInstance().getReference().child(uid).child("Expense").orderByChild("week").equalTo(week);
        query4.addValueEventListener(new ValueEventListener() {                                                                      //Method for Expense per Week
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double totalExpenseWeek = 0.0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    totalExpenseWeek += Double.parseDouble(String.valueOf(ds.child("value").getValue()));
                }
                mBudgetText7.setText(String.valueOf("- " + totalExpenseWeek));
                Log.d("TAG", "totalExpenseWeek " + String.valueOf(totalExpenseWeek));

                expenseWeek += totalExpenseWeek;
                Log.d("TAG", "expenseWeek " + String.valueOf(expenseWeek));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Query query5 = FirebaseDatabase.getInstance().getReference().child(uid).child("Income").orderByChild("incomeMonth").equalTo(month);
        query5.addValueEventListener(new ValueEventListener() {                                                                            //Method for Income per Month
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double totalIncomeMonth = 0.0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    totalIncomeMonth += Double.parseDouble(String.valueOf(ds.child("incomeValue").getValue()));
                }
                mBudgetText8.setText(String.valueOf(totalIncomeMonth));
                Log.d("TAG", "totalIncomeMonth " + String.valueOf(totalIncomeMonth));

                incomeMonth += totalIncomeMonth;
                Log.d("TAG", "incomeMonth " + String.valueOf(incomeMonth));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Query query6 = FirebaseDatabase.getInstance().getReference().child(uid).child("Expense").orderByChild("month").equalTo(month);
        query6.addValueEventListener(new ValueEventListener() {                                                                       //Method for Expense per Month
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double totalExpenseMonth = 0.0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    totalExpenseMonth += Double.parseDouble(String.valueOf(ds.child("value").getValue()));
                }
                mBudgetText9.setText(String.valueOf("- " + totalExpenseMonth));
                Log.d("TAG", "totalExpenseMonth " + String.valueOf(totalExpenseMonth));

                expenseMonth += totalExpenseMonth;
                Log.d("TAG", "expenseMonth " + String.valueOf(expenseMonth));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {        // Method created for counting sum of budget
                DecimalFormat df = new DecimalFormat("0.00");                                 // by day, week and month

                mBudgetText10.setText(df.format(incomeDay - expenseDay));
                if(incomeDay < expenseDay) {
                    mBudgetText10.setTextColor(Color.parseColor("#ab0808"));
                }
                else if(incomeDay > expenseDay) {
                    mBudgetText10.setTextColor(Color.parseColor("#22af15"));
                }

                mBudgetText11.setText(df.format(incomeWeek - expenseWeek));
                if(incomeWeek < expenseWeek) {
                    mBudgetText11.setTextColor(Color.parseColor("#ab0808"));
                }
                else if (incomeWeek > expenseWeek) {
                    mBudgetText11.setTextColor(Color.parseColor("#22af15"));
                }

                mBudgetText12.setText(df.format(incomeMonth - expenseMonth));
                if (incomeMonth < expenseMonth) {
                    mBudgetText12.setTextColor(Color.parseColor("#ab0808"));
                }
                else if (incomeMonth > expenseMonth) {
                    mBudgetText12.setTextColor(Color.parseColor("#22af15"));
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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
