package com.example.daniellachacz.homebudget;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Objects;


public class MainActivity extends AppCompatActivity implements HomeFragment.Expense, HomeFragment.Income {

    private static final String TAG = "ViewDatabase";

    private DatabaseReference myRef;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    private HomeFragment homeFragment;
    private ListFragment listFragment;
    private BudgetFragment budgetFragment;



    private EditText mDescriptionField;
    private EditText mDateField;
    private EditText mValueField;
    private Button mSaveButton;

    private ListView mListView;
    private ListView mListView2;

    private String userID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,
                new HomeFragment()).commit();

        homeFragment = new HomeFragment();
        listFragment = new ListFragment();
        budgetFragment = new BudgetFragment();
        mListView = findViewById(R.id.myListView);
        mListView2 = findViewById(R.id.myListView2);

        myRef = FirebaseDatabase.getInstance().getReference().child("");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null) {

                    Intent loginIntent = new Intent(MainActivity.this, RegisterActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };

    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {

                case R.id.navigation_home:
                    selectedFragment = new HomeFragment();
                    break;

                case R.id.navigation_list:
                    selectedFragment = new ListFragment();
                    break;

                case R.id.navigation_budget:
                    selectedFragment = new BudgetFragment();
                    break;


            }

            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,
                    selectedFragment).commit();

            return true;
        }
    };



    @Override
    protected void onStart(){
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {

            logout();

        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {

        mAuth.signOut();
    }


    @Override
    public void addExpense(String description, String date, Double value) {


            mDescriptionField = findViewById(R.id.descriptionField);
            mDateField = findViewById(R.id.dateField);
            mValueField = findViewById(R.id.valueField);

            Expense expense = new Expense(description, date, value);

            String user_id = (Objects.requireNonNull(mAuth.getCurrentUser())).getUid();

            myRef.child(user_id).child("Expense").push().setValue(expense);


        }


    @Override
    public void addIncome(String incomeDescription, String incomeDate, Double incomeValue) {

        mDescriptionField = findViewById(R.id.descriptionField);
        mDateField = findViewById(R.id.dateField);
        mValueField = findViewById(R.id.valueField);

        Income income = new Income(incomeDescription, incomeDate, incomeValue);

        String user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        myRef.child(user_id).child("Income").push().setValue(income);
    }

}




