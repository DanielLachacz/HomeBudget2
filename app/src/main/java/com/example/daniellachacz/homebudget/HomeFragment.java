package com.example.daniellachacz.homebudget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class HomeFragment extends Fragment implements View.OnClickListener {


    private EditText mDescriptionField;
    private EditText mDateField;
    private EditText mValueField;

    private DatabaseReference myRef;

    Expense addExpense;
    Income addIncome;




    public interface Expense {

        void addExpense(String description, String date, Double value);

    }

    public interface Income {

        void addIncome(String incomeDescription, String incomeDate, Double incomeValue);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        myRef = FirebaseDatabase.getInstance().getReference().child("Expense");

        mDescriptionField = v.findViewById(R.id.descriptionField);
        mDateField = v.findViewById(R.id.dateField);
        mValueField = v.findViewById(R.id.valueField);
        Button SaveButton = v.findViewById(R.id.saveButton);
        Button IncomeButton = v.findViewById(R.id.incomeButton);
        SaveButton.setOnClickListener(this);
        IncomeButton.setOnClickListener(this);

       return v;

    }

    @Override
    public void onClick(View v) {
        int viedID = v.getId();
        switch (viedID) {

            case R.id.saveButton:

                String description = mDescriptionField.getText().toString();
                String date = mDateField.getText().toString();
                Double value = Double.valueOf(mValueField.getText().toString());

                if(!description.equals("") && !date.equals("") && !value.equals("")) {

                    addExpense.addExpense(description, date, value);

                    Toast.makeText(getActivity(), "Dodano wydatek", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getActivity(), "Proszę uzupełnić wszystkie pola", Toast.LENGTH_LONG).show();
                }
             break;

            case R.id.incomeButton:


                String incomeDescription = mDescriptionField.getText().toString();
                String incomeDate = mDateField.getText().toString();
                Double incomeValue = Double.valueOf(mValueField.getText().toString());

                if(!incomeDescription.equals("") && !incomeDate.equals("") && !incomeValue.equals("")) {

                    addIncome.addIncome(incomeDescription, incomeDate, incomeValue);

                    Toast.makeText(getActivity(), "Dodano przychód", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getActivity(), "Proszę uzupełnić wszystkie pola", Toast.LENGTH_LONG).show();
                }
             break;
        }

    }

    @Override
    public void onAttach(Context context) throws ClassCastException {
        super.onAttach(context);

       try{
           addExpense = (Expense) context;
           addIncome = (Income) context;
       } catch (ClassCastException e) {
           throw new ClassCastException(context.toString());
       }

    }
}
