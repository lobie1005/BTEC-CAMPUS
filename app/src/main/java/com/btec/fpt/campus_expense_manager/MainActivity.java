package com.btec.fpt.campus_expense_manager;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.btec.fpt.campus_expense_manager.fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.btec.fpt.campus_expense_manager.fragments.AddExpenseFragment;
import com.btec.fpt.campus_expense_manager.fragments.ForgotPasswordFragment;
import com.btec.fpt.campus_expense_manager.fragments.LoginFragment;
import com.btec.fpt.campus_expense_manager.fragments.RegisterFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        FloatingActionButton fabDashboard = findViewById(R.id.fab_dashboard);
        HomeFragment homeFragment = new HomeFragment();
        GoalFragment goalFragment = new GoalFragment();

        // Load default fragment (LoginFragment)
        loadFragment(new LoginFragment());

        // Handle navigation item clicks
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch ((item.getItemId())){
                case R.id.fragment_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.fragment_goals:
                    selectedFragment = new GoalFragment();
                    break;
                case R.id.fragment_forgot_password:
                    selectedFragment = new ForgotPasswordFragment();
                    break;
                case R.id.fragment_register:
                    selectedFragment = new RegisterFragment();
                    break;

            }

        }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
