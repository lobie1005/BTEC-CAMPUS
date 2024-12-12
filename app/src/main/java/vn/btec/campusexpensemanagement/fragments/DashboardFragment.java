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

        return view;
    }

    private void initializeViews(View view) {
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
    }
}
