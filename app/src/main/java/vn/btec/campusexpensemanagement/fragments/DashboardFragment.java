package vn.btec.campusexpensemanagement.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DashboardFragment extends Fragment {
    private DatabaseHelper databaseHelper;
    private TextView tvFullName, tvBalance, tvTotalIncome, tvTotalExpense, tvMonthlyOverview;
    private ProgressBar progressBudgetRemain;
    private RecyclerView rvRecentTransactions;
    private View btnAddIncome, btnAddExpense;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Initialize Components
        initializeComponents(view);

        // Load Dashboard Data
        loadDashboardData();

        // Load Transactions
        loadTransactions();

        // Setup Listeners
        setupListeners();

        return view;
    }

    private void initializeComponents(View view) {
        databaseHelper = new DatabaseHelper(requireContext());
        tvFullName = view.findViewById(R.id.tvFullName);
        tvBalance = view.findViewById(R.id.tvBalance);
        tvTotalIncome = view.findViewById(R.id.tvTotalIncome);
        tvTotalExpense = view.findViewById(R.id.tvTotalExpense);
        tvMonthlyOverview = view.findViewById(R.id.tvMonthlyOverview);
        rvRecentTransactions = view.findViewById(R.id.rvRecentTransactions);
        btnAddIncome = view.findViewById(R.id.btnAddIncome);
        btnAddExpense = view.findViewById(R.id.addButton);

        // Initialize RecyclerView
        if (rvRecentTransactions != null) {
            rvRecentTransactions.setLayoutManager(new LinearLayoutManager(requireContext()));
        }
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

    private void loadTransactions() {
        String currentUserEmail = databaseHelper.getCurrentUserEmail();
        if (currentUserEmail == null) return;

        // Get recent transactions
        List<Transaction> transactions = databaseHelper.getRecentTransactions(currentUserEmail, 5);
        
        if (transactions != null && !transactions.isEmpty()) {
            // Update transaction list
            TransactionAdapter adapter = new TransactionAdapter(requireContext(), transactions);
            if (rvRecentTransactions != null) {
                rvRecentTransactions.setAdapter(adapter);
            }
        }
    }

    private void setupListeners() {
        if (btnAddIncome != null) {
            btnAddIncome.setOnClickListener(v -> {
                if (getActivity() instanceof MainActivity) {
                    AddIncomeFragment addIncomeFragment = new AddIncomeFragment();
                    ((MainActivity) getActivity()).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, addIncomeFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }

        if (btnAddExpense != null) {
            btnAddExpense.setOnClickListener(v -> {
                if (getActivity() instanceof MainActivity) {
                    AddExpenseFragment addExpenseFragment = new AddExpenseFragment();
                    ((MainActivity) getActivity()).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, addExpenseFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }

        if (tvMonthlyOverview != null) {
            tvMonthlyOverview.setOnClickListener(v -> {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).navigateToReportWithFilter("Expense");
                }
            });
        }
    }

    private void navigateToAddIncome() {
        if (getActivity() instanceof MainActivity) {
            AddIncomeFragment addIncomeFragment = new AddIncomeFragment();
            ((MainActivity) getActivity()).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, addIncomeFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void navigateToAddExpense() {
        if (getActivity() instanceof MainActivity) {
            AddExpenseFragment addExpenseFragment = new AddExpenseFragment();
            ((MainActivity) getActivity()).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, addExpenseFragment)
                    .addToBackStack(null)
                    .commit();
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
        loadTransactions();
    }

}
