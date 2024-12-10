package vn.btec.campusexpensemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import vn.btec.campusexpensemanagement.fragments.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ReportFragment reportFragment;
    private DashboardFragment dashboardFragment;
    private FloatingActionButton fabAddExpense;
    private PlaceholderFragment placeholderFragment;

    // DataStatic object to check the login status
    private DataStatic dataStatic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize DataStatic
        dataStatic = new DataStatic();

        // Check if the user is logged in
        if (!dataStatic.isLogin) {
            // If not logged in, redirect to LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();  // Close MainActivity so the user can't go back to it
            return;  // Exit onCreate
        }

        setContentView(R.layout.activity_main);

        // Initialize fragments
        reportFragment = new ReportFragment();
        dashboardFragment = new DashboardFragment();
        placeholderFragment = new PlaceholderFragment();

        // Initialize views
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Set default fragment to DashboardFragment
        if (savedInstanceState == null) {
            // Only load the fragment if it's not recreated due to screen rotation or similar
            loadFragment(dashboardFragment);
        }

        // Setup bottom navigation
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();

                if (itemId == R.id.fragment_dashboard) {
                    selectedFragment = dashboardFragment;
                } else if (itemId == R.id.fragment_report) {
                    selectedFragment = reportFragment;
                } else if (itemId == R.id.fragment_goals) {
                    selectedFragment = new GoalFragment();
                } else if (itemId == R.id.fragment_settings) {
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
    }

    // Method to load fragment
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Ensure that the fragment container exists
        View container = findViewById(R.id.fragment_container);
        if (container == null) {
            Log.e("FragmentError", "No fragment container found in the layout.");
            return;
        }

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null); // Optional: Allows the user to go back to previous fragment
        fragmentTransaction.commit();
    }

    // Method to pass transaction type from Dashboard to Report
    public void navigateToReportWithFilter(String transactionType) {
        // Ensure the ReportFragment is the current fragment
        loadFragment(reportFragment);

        // Set the bottom navigation to the report item
        bottomNavigationView.setSelectedItemId(R.id.fragment_report);

        // Set the initial filter in the ReportFragment
        reportFragment.setInitialFilter(transactionType);
    }
}
