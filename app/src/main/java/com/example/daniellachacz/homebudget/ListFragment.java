package com.example.daniellachacz.homebudget;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Calendar;
import java.util.Objects;


public class ListFragment extends Fragment {


    private TextView mTextDate;
    private TextView mTextDescription;
    private TextView mTextValue;
    private TextView mTextDate2;
    private TextView mTextDescription2;
    private TextView mTextValue2;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private static final String TAG = "ListFragment";
    private FirebaseListAdapter listAdapter;
    private FirebaseListAdapter listAdapter2;

    static Calendar cal = Calendar.getInstance();
    int year = cal.get(Calendar.YEAR); // get the current year
    int month = cal.get(Calendar.MONTH); // month...
    int week = cal.get(Calendar.WEEK_OF_YEAR);
    int day = cal.get(Calendar.DAY_OF_MONTH);


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_list, container, false);

        ListView listView = v.findViewById(R.id.myListView);
        ListView listView2 = v.findViewById(R.id.myListView2);

        mTextDescription = v.findViewById(R.id.textDescription);
        mTextDate = v.findViewById(R.id.textDate);
        mTextValue = v.findViewById(R.id.textValue);

        mTextDescription2 = v.findViewById(R.id.textDescription2);
        mTextDate2 = v.findViewById(R.id.textDate2);
        mTextValue2 = v.findViewById(R.id.textValue2);

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

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

        Query query = FirebaseDatabase.getInstance().getReference().child(uid).child("Expense");
        FirebaseListOptions<Expense> options = new FirebaseListOptions.Builder<Expense>()
                .setLayout(R.layout.list_item)
                .setQuery(query, Expense.class)
                .build();
        listAdapter = new FirebaseListAdapter<Expense>(options) {
            @Override
            protected void populateView(View v, Expense model, int position) {

                TextView textDescription;
                TextView textDate;
                TextView textValue;

                textDescription = v.findViewById(R.id.textDescription);
                textDate = v.findViewById(R.id.textDate);
                textValue = v.findViewById(R.id.textValue);

                textDescription.setText(model.getDescription());
                textDate.setText(model.getDate());
                textValue.setText("- " + model.getValue());

            }
        };
        listView.setAdapter(listAdapter);
        mRef.keepSynced(true);


        Query query2 = FirebaseDatabase.getInstance().getReference().child(uid).child("Income");
        FirebaseListOptions<Income> options2 = new FirebaseListOptions.Builder<Income>()
                .setLayout(R.layout.list_item2)
                .setQuery(query2, Income.class)
                .build();
        listAdapter2 = new FirebaseListAdapter<Income>(options2) {
            @Override
            protected void populateView(View v, Income model, int position) {

                TextView textDescription2;
                TextView textDate2;
                TextView textValue2;

                textDescription2 = v.findViewById(R.id.textDescription2);
                textDate2 = v.findViewById(R.id.textDate2);
                textValue2 = v.findViewById(R.id.textValue2);

                textDescription2.setText(model.getIncomeDescription());
                textDate2.setText(model.getIncomeDate());
                textValue2.setText(String.valueOf(model.getIncomeValue()));

            }
        };
        listView2.setAdapter(listAdapter2);
        mRef.keepSynced(true);
        return v;

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
        listAdapter.startListening();
        listAdapter2.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        listAdapter.stopListening();
        listAdapter2.stopListening();

    }



    @Override
    public void onResume() {
        super.onResume();
        if (null != mAuthListener) {
            mAuth.addAuthStateListener(mAuthListener);
        }
    }


}
