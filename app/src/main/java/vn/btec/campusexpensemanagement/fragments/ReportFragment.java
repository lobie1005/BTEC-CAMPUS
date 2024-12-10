package vn.btec.campusexpensemanagement.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import vn.btec.campusexpensemanagement.R;
import vn.btec.campusexpensemanagement.adapters.TransactionAdapter;
import vn.btec.campusexpensemanagement.database.DatabaseHelper;
import vn.btec.campusexpensemanagement.entities.Transaction;
import vn.btec.campusexpensemanagement.utils.CurrencyUtils;
import vn.btec.campusexpensemanagement.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ReportFragment extends Fragment {
    private DatabaseHelper databaseHelper;
    private TextView tvTotalIncome, tvTotalExpense;
    private RecyclerView rvTransactions;
    private Spinner spinnerTimePeriod, spinnerTransactionType, spinnerSortOption;
    private TransactionAdapter transactionAdapter;
    private List<Transaction> transactions;

    // Constants for filtering and sorting
    private static final String[] TIME_PERIODS = {
            "Current Month", "Last 7 Days", "Last 30 Days",
            "Last 3 Months", "Last 6 Months", "Last Year"
    };
    private static final String[] TRANSACTION_TYPES = { "All", "Income", "Expense" };
    private static final String[] SORT_OPTIONS = {
            "Date (New to Old)", "Date (Old to New)",
            "Amount (High to Low)", "Amount (Low to High)",
            "Alphabetical (A-Z)", "Alphabetical (Z-A)"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        initializeComponents(view);
        setupSpinners();
        loadInitialTransactions();
        return view;
    }

    private void initializeComponents(View view) {
        databaseHelper = new DatabaseHelper(requireContext());

        // Initialize UI components
        tvTotalIncome = view.findViewById(R.id.tvTotalIncome);
        tvTotalExpense = view.findViewById(R.id.tvTotalExpense);
        rvTransactions = view.findViewById(R.id.rvTransactions);
        spinnerTimePeriod = view.findViewById(R.id.spinnerTimePeriod);
        spinnerTransactionType = view.findViewById(R.id.spinnerTransactionType);
        spinnerSortOption = view.findViewById(R.id.spinnerSortOption);

        // Setup RecyclerView
        transactions = new ArrayList<>();
        transactionAdapter = new TransactionAdapter(transactions);
        rvTransactions.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvTransactions.setAdapter(transactionAdapter);
    }

    private void setupSpinners() {
        setupSpinner(spinnerTimePeriod, TIME_PERIODS);
        setupSpinner(spinnerTransactionType, TRANSACTION_TYPES);
        setupSpinner(spinnerSortOption, SORT_OPTIONS);

        // Set common listener for all spinners
        AdapterView.OnItemSelectedListener filterListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterTransactions();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };

        spinnerTimePeriod.setOnItemSelectedListener(filterListener);
        spinnerTransactionType.setOnItemSelectedListener(filterListener);
        spinnerSortOption.setOnItemSelectedListener(filterListener);
    }

    private void setupSpinner(Spinner spinner, String[] items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void loadInitialTransactions() {
        String email = databaseHelper.getCurrentUserEmail();
        String selectedType = spinnerTransactionType.getSelectedItem() != null
                ? spinnerTransactionType.getSelectedItem().toString()
                : "";

        // Convert "All" to empty string to match our new database query logic
        if ("All".equals(selectedType)) {
            selectedType = "";
        }

        transactions = databaseHelper.getCurrentMonthTransactions(email, selectedType);
        updateTransactionList();
        updateTransactionSummary();
    }

    private void filterTransactions() {
        String email = databaseHelper.getCurrentUserEmail();

        // Determine time period
        long[] timePeriod = getSelectedTimePeriod();

        // Determine transaction type
        String transactionType = getSelectedTransactionType();

        // Determine sort option
        DatabaseHelper.TransactionSortOption sortOption = getSelectedSortOption();

        transactions = databaseHelper.getTransactionsWithFilter(
                email,
                null, // Assuming type filter is not used here
                transactionType,
                String.valueOf(timePeriod[0]), // Convert long to String
                String.valueOf(timePeriod[1]), // Convert long to String
                null, // Assuming minAmount filter is not used here
                null, // Assuming maxAmount filter is not used here
                sortOption
        );

        updateTransactionList();
        updateTransactionSummary();
    }

    private long[] getSelectedTimePeriod() {
        Calendar calendar = Calendar.getInstance();
        long startDate, endDate;

        switch (spinnerTimePeriod.getSelectedItemPosition()) {
            case 0: // Current Month
                return DateUtils.getCurrentMonthTimestamps();
            case 1: // Last 7 Days
                calendar.add(Calendar.DAY_OF_MONTH, -7);
                startDate = calendar.getTimeInMillis();
                endDate = System.currentTimeMillis();
                break;
            case 2: // Last 30 Days
                calendar.add(Calendar.DAY_OF_MONTH, -30);
                startDate = calendar.getTimeInMillis();
                endDate = System.currentTimeMillis();
                break;
            case 3: // Last 3 Months
                calendar.add(Calendar.MONTH, -3);
                startDate = calendar.getTimeInMillis();
                endDate = System.currentTimeMillis();
                break;
            case 4: // Last 6 Months
                calendar.add(Calendar.MONTH, -6);
                startDate = calendar.getTimeInMillis();
                endDate = System.currentTimeMillis();
                break;
            case 5: // Last Year
                calendar.add(Calendar.YEAR, -1);
                startDate = calendar.getTimeInMillis();
                endDate = System.currentTimeMillis();
                break;
            default:
                startDate = 0;
                endDate = System.currentTimeMillis();
        }
        return new long[]{startDate, endDate};
    }

    private String getSelectedTransactionType() {
        switch (spinnerTransactionType.getSelectedItemPosition()) {
            case 1: // Income
                return "Income";
            case 2: // Expense
                return "Expense";
            default:
                return ""; // Return empty string for "All"
        }
    }

    private DatabaseHelper.TransactionSortOption getSelectedSortOption() {
        switch (spinnerSortOption.getSelectedItemPosition()) {
            case 0: // Date (New to Old)
                return DatabaseHelper.TransactionSortOption.DATE_NEW_TO_OLD;
            case 1: // Date (Old to New)
                return DatabaseHelper.TransactionSortOption.DATE_OLD_TO_NEW;
            case 2: // Amount (High to Low)
                return DatabaseHelper.TransactionSortOption.AMOUNT_HIGH_TO_LOW;
            case 3: // Amount (Low to High)
                return DatabaseHelper.TransactionSortOption.AMOUNT_LOW_TO_HIGH;
            case 4: // Alphabetical (A-Z)
                return DatabaseHelper.TransactionSortOption.ALPHABETICAL_ASC;
            case 5: // Alphabetical (Z-A)
                return DatabaseHelper.TransactionSortOption.ALPHABETICAL_DESC;
            default:
                return DatabaseHelper.TransactionSortOption.DATE_NEW_TO_OLD;
        }
    }

    private void updateTransactionList() {
        transactionAdapter.updateTransactions(transactions);
    }

    private void updateTransactionSummary() {
        double totalIncome = transactions.stream()
                .filter(t -> t.getType() == 1)
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalExpense = transactions.stream()
                .filter(t -> t.getType() == 2)
                .mapToDouble(Transaction::getAmount)
                .sum();

        // Use Vietnamese locale for currency formatting
        tvTotalIncome.setText(CurrencyUtils.formatCurrency(totalIncome, new Locale("vi", "VN")));
        tvTotalExpense.setText(CurrencyUtils.formatCurrency(totalExpense, new Locale("vi", "VN")));
    }

    // Method to receive data from Dashboard Fragment
    public void setInitialFilter(String transactionType) {
        int typePosition;
        if (transactionType.equals("Income")) {
            typePosition = 1;
        } else if (transactionType.equals("Expense")) {
            typePosition = 2;
        } else {
            typePosition = 0; // All
        }

        spinnerTransactionType.setSelection(typePosition);
        filterTransactions();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}
