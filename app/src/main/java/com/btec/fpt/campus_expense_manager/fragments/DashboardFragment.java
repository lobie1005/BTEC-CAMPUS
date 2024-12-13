package com.btec.fpt.campus_expense_manager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import vn.lobie.campusss.R;
import vn.lobie.campusss.database.DatabaseHelper;
import vn.lobie.campusss.database.SessionManager;

public class DashboardFragment extends Fragment {
    private TextView tvMonthlyOverview;
    private TextView tvRemainingBudget;
    private FloatingActionButton fabAddExpense;
    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dashboard, container, false);

        // Initialize views
        tvMonthlyOverview = view.findViewById(R.id.tvMonthlyOverview);
        tvRemainingBudget = view.findViewById(R.id.tvRemainingBudget);
        fabAddExpense = view.findViewById(R.id.fabAddExpense);

        // Initialize helpers
        databaseHelper = new DatabaseHelper(requireContext());
        sessionManager = new SessionManager(requireContext());

        setupFabButton();
        updateDashboardData();

        return view;
    }

    private void setupFabButton() {
        fabAddExpense.setOnClickListener(v -> {
            // Add a test expense
            databaseHelper.addExpense("Test Expense", 50.0, "Other", sessionManager.getUserEmail());
            updateDashboardData(); // Refresh the dashboard
        });
    }

    private void updateDashboardData() {
        String userEmail = sessionManager.getUserEmail();
        double totalSpent = databaseHelper.getTotalExpensesForCurrentMonth(userEmail);
        double monthlyBudget = sessionManager.getMonthlyBudget();

        // Update total spent
        tvMonthlyOverview.setText(String.format("Đã chi: %.2f₫", totalSpent));

        // Update remaining budget if a budget is set
        if (monthlyBudget > 0) {
            double remaining = monthlyBudget - totalSpent;
            tvRemainingBudget.setText(String.format("Còn lại: %.2f₫ / %.2f₫", remaining, monthlyBudget));
        } else {
            tvRemainingBudget.setText("Chưa thiết lập ngân sách tháng");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateDashboardData();
    }
}
