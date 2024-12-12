<<<<<<< Updated upstream:app/src/main/java/vn/btec/campusexpensemanagement/fragments/DashboardFragment.java
package vn.btec.campusexpensemanagement.fragments;
=======
package com.btec.fpt.campus_expense_manager.fragments;
>>>>>>> Stashed changes:app/src/main/java/com/btec/fpt/campus_expense_manager/fragments/DashboardFragment.java

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
<<<<<<< Updated upstream:app/src/main/java/vn/btec/campusexpensemanagement/fragments/DashboardFragment.java
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import vn.btec.campusexpensemanagement.MainActivity;
import vn.btec.campusexpensemanagement.R;
import vn.btec.campusexpensemanagement.adapters.TransactionAdapter;
import vn.btec.campusexpensemanagement.database.DatabaseHelper;
import vn.btec.campusexpensemanagement.entities.Transaction;
import vn.btec.campusexpensemanagement.entities.User;
import vn.btec.campusexpensemanagement.fragments.AddExpenseFragment;
import vn.btec.campusexpensemanagement.fragments.AddIncomeFragment;
import vn.btec.campusexpensemanagement.utils.CurrencyUtils;
import vn.btec.campusexpensemanagement.DataStatic;
=======

import com.btec.fpt.campus_expense_manager.MainActivity;
import com.btec.fpt.campus_expense_manager.R;
import com.btec.fpt.campus_expense_manager.database.DatabaseHelper;
import com.btec.fpt.campus_expense_manager.entities.Transaction;
import com.btec.fpt.campus_expense_manager.entities.User;
>>>>>>> Stashed changes:app/src/main/java/com/btec/fpt/campus_expense_manager/fragments/DashboardFragment.java

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DashboardFragment extends Fragment {
<<<<<<< Updated upstream:app/src/main/java/vn/btec/campusexpensemanagement/fragments/DashboardFragment.java
    private RecyclerView rvRecentTransactions;
    private PieChart chartOverview;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        initializeViews(view);
        setupTransactionsList();
        loadDashboardData();
        setupChart();
=======
    private DatabaseHelper databaseHelper;
    private TextView tvFullName, tvBalance, tvTotalIncome, tvTotalExpense, tvMonthlyOverview;
    private ProgressBar progressBudgetRemain;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(requireContext());

        // Initialize Views
        initializeViews(view);

        // Load Dashboard Data
        loadDashboardData();

        // Setup Monthly Overview Click Listener
        tvMonthlyOverview.setOnClickListener(v -> {
            // Navigate to Report Fragment with Expense filter
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToReportWithFilter("Expense");
            }
        });
>>>>>>> Stashed changes:app/src/main/java/com/btec/fpt/campus_expense_manager/fragments/DashboardFragment.java

        return view;
    }

    private void initializeViews(View view) {
<<<<<<< Updated upstream:app/src/main/java/vn/btec/campusexpensemanagement/fragments/DashboardFragment.java
        tvBalance = view.findViewById(R.id.tvBalance);
        tvIncome = view.findViewById(R.id.tvIncome);
        tvExpense = view.findViewById(R.id.tvExpense);
        rvRecentTransactions = view.findViewById(R.id.rvRecentTransactions);
        chartOverview = view.findViewById(R.id.chartOverview);

        dbHelper = DatabaseManager.getInstance(requireContext()).getHelper();
        sessionManager = SessionManager.getInstance(requireContext());
    }

    private void loadDashboardData() {
        String email = sessionManager.getCurrentUserEmail();
        if (email != null) {
            double totalIncome = dbHelper.getTotalIncome(email);
            double totalExpense = dbHelper.getTotalExpense(email);
            double balance = totalIncome - totalExpense;

            tvBalance.setText(String.format("$%.2f", balance));
            tvIncome.setText(String.format("$%.2f", totalIncome));
            tvExpense.setText(String.format("$%.2f", totalExpense));
        }
    }

    private void setupTransactionsList() {
        // Initialize adapter and setup RecyclerView
    }

    private void setupChart() {
        // Setup PieChart with income/expense data
=======
        // User Info Views
        tvFullName = view.findViewById(R.id.tvFullName);
        tvBalance = view.findViewById(R.id.tvBalance);
        progressBudgetRemain = view.findViewById(R.id.progressBudgetRemain);

        // Transaction Views
        tvTotalIncome = view.findViewById(R.id.tvTotalIncome);
        tvTotalExpense = view.findViewById(R.id.tvTotalExpense);
        tvMonthlyOverview = view.findViewById(R.id.tvMonthlyOverview);
    }

    private void loadDashboardData() {
        // Get current user's email from SharedPreferences
        String currentUserEmail = databaseHelper.getCurrentUserEmail();
        if (currentUserEmail == null) return;

        // Get current user details
        User currentUser = databaseHelper.getUserByEmail(currentUserEmail);
        if (currentUser != null) {
            // Set Full Name
            tvFullName.setText(currentUser.getFirstName() + " " + currentUser.getLastName());

            // Get current month's transactions
            List<Transaction> monthlyTransactions = databaseHelper.getCurrentMonthTransactions(currentUserEmail, null);

            // Calculate total income and expense
            double totalIncome = 0;
            double totalExpense = 0;

            for (Transaction transaction : monthlyTransactions) {
                if (transaction.getType() == 1) { // Income
                    totalIncome += transaction.getAmount();
                } else { // Expense
                    totalExpense += transaction.getAmount();
                }
            }

            // Format currency
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

            // Calculate balance and budget remaining percentage
            double balance = totalIncome - totalExpense;
            double budgetLimit = calculateBudgetLimit(currentUserEmail);
            int budgetRemainingPercent = calculateBudgetRemainingPercent(balance, budgetLimit);

            // Set views
            tvBalance.setText(currencyFormat.format(balance));
            tvTotalIncome.setText(currencyFormat.format(totalIncome));
            tvTotalExpense.setText(currencyFormat.format(totalExpense));
            progressBudgetRemain.setProgress(budgetRemainingPercent);

            // Set monthly overview text
            String overviewText = balance >= 0
                    ? "You saved " + currencyFormat.format(balance) + " this month"
                    : "You overspent " + currencyFormat.format(Math.abs(balance)) + " this month";
            tvMonthlyOverview.setText(overviewText);
        }
    }

    private double calculateBudgetLimit(String email) {
        // TODO: Implement method to retrieve user's budget limit from database
        // This might involve adding a method to DatabaseHelper to get user's budget settings
        return 10000.0; // Placeholder value
    }

    private int calculateBudgetRemainingPercent(double currentBalance, double budgetLimit) {
        if (budgetLimit <= 0) return 0;
        
        double remainingPercent = (currentBalance / budgetLimit) * 100;
        return Math.min(Math.max((int) remainingPercent, 0), 100);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload dashboard data when fragment becomes visible
        loadDashboardData();
>>>>>>> Stashed changes:app/src/main/java/com/btec/fpt/campus_expense_manager/fragments/DashboardFragment.java
    }
}
