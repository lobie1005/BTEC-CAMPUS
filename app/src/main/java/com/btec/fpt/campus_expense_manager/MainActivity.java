package com.btec.fpt.campus_expense_manager;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.btec.fpt.campus_expense_manager.fragments.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ReportFragment reportFragment;
    private DashboardFragment dashboardFragment;
    private FloatingActionButton fabAddExpense;
    private PlaceholderFragment placeholderFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize fragments
        reportFragment = new ReportFragment();
        dashboardFragment = new DashboardFragment();
        placeholderFragment = new PlaceholderFragment();

        // Initialize views
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fabAddExpense = findViewById(R.id.fabAddExpense);

        // Set default fragment to DashboardFragment
        loadFragment(dashboardFragment);

        // Setup bottom navigation
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();

                if (itemId == R.id.fragment_dashboard) {
                    selectedFragment = dashboardFragment;
                } else if (itemId == R.id.fragment_reports) {
                    selectedFragment = reportFragment;
                } else if (itemId == R.id.fragment_goals) {
                    selectedFragment = new GoalFragment();
                } else if (itemId == R.id.fragement_settings) {
                    selectedFragment = new SettingsFragment();
                } else if (itemId == R.id.fragment_transactions) {
                    selectedFragment = placeholderFragment;
                }

                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                    return true;
                }
                return false;
            }
        });

        // Handle FAB click
        fabAddExpense.setOnClickListener(view -> {
            loadFragment(new AddExpenseFragment());
        });
    }

    // Method to load fragment
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    // Method to pass transaction type from Dashboard to Report
    public void navigateToReportWithFilter(String transactionType) {
        // Ensure the ReportFragment is the current fragment
        loadFragment(reportFragment);
        
        // Set the bottom navigation to the report item
        bottomNavigationView.setSelectedItemId(R.id.fragment_reports);
        
        // Set the initial filter in the ReportFragment
        reportFragment.setInitialFilter(transactionType);
    }
}
