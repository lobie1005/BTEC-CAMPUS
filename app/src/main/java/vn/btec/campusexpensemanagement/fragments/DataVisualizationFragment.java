package com.btec.fpt.campus_expense_manager.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.btec.fpt.campus_expense_manager.R;
import com.btec.fpt.campus_expense_manager.database.DatabaseHelper;
import com.btec.fpt.campus_expense_manager.entities.Transaction;
import com.btec.fpt.campus_expense_manager.models.BalanceInfor;
import com.btec.fpt.campus_expense_manager.utils.CurrencyUtils;
import com.btec.fpt.campus_expense_manager.utils.DateUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class DataVisualizationFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private PieChart pieChartTransactionTypes;
    private BarChart barChartMonthlyTransactions;
    private TextView tvTotalIncome, tvTotalExpense, tvNetBalance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_visualization, container, false);

        initializeComponents(view);
        setupCharts();

        return view;
    }

    private void initializeComponents(View view) {
        databaseHelper = new DatabaseHelper(requireContext());

        pieChartTransactionTypes = view.findViewById(R.id.pieChartTransactionTypes);
        barChartMonthlyTransactions = view.findViewById(R.id.barChartMonthlyTransactions);
        tvTotalIncome = view.findViewById(R.id.tvTotalIncome);
        tvTotalExpense = view.findViewById(R.id.tvTotalExpense);
        tvNetBalance = view.findViewById(R.id.tvNetBalance);
    }

    private void setupCharts() {
        String currentUserEmail = databaseHelper.getCurrentUserEmail();
        
        // Use existing method to get balance information
        BalanceInfor balanceInfo = databaseHelper.getBalanceInfor(currentUserEmail);
        
        // Update summary text views
        updateSummaryTextViews(balanceInfo);

        // Pie Chart: Transaction Types
        setupPieChartTransactionTypes(currentUserEmail);

        // Bar Chart: Monthly Transactions
        setupBarChartMonthlyTransactions(currentUserEmail);
    }

    private void updateSummaryTextViews(BalanceInfor balanceInfo) {
        tvTotalIncome.setText(String.format("Total Income: %s", 
            CurrencyUtils.formatCurrency(balanceInfo.getTotalIncome())));
        tvTotalExpense.setText(String.format("Total Expense: %s", 
            CurrencyUtils.formatCurrency(balanceInfo.getTotalExpense())));
        tvNetBalance.setText(String.format("Net Balance: %s", 
            CurrencyUtils.formatCurrency(balanceInfo.getNetBalance())));
    }

    private void setupPieChartTransactionTypes(String email) {
        List<Transaction> transactions = databaseHelper.getAllTransactionsByEmail(email);
        
        double totalIncome = transactions.stream()
            .filter(t -> t.getType() == Transaction.INCOME_TYPE)
            .mapToDouble(Transaction::getAmount)
            .sum();

        double totalExpense = transactions.stream()
            .filter(t -> t.getType() != Transaction.INCOME_TYPE)
            .mapToDouble(Transaction::getAmount)
            .sum();

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry((float)totalIncome, "Income"));
        entries.add(new PieEntry((float)totalExpense, "Expense"));

        PieDataSet dataSet = createPieDataSet(entries);
        setupPieChart(dataSet);
    }

    private PieDataSet createPieDataSet(List<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, "Transaction Types");
        dataSet.setColors(new int[]{Color.GREEN, Color.RED});
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(12f);
        return dataSet;
    }

    private void setupPieChart(PieDataSet dataSet) {
        PieData pieData = new PieData(dataSet);
        pieChartTransactionTypes.setData(pieData);
        pieChartTransactionTypes.getDescription().setEnabled(false);
        pieChartTransactionTypes.animateY(1000);
        pieChartTransactionTypes.invalidate();
    }

    private void setupBarChartMonthlyTransactions(String email) {
        List<BarEntry> incomeEntries = new ArrayList<>();
        List<BarEntry> expenseEntries = new ArrayList<>();
        List<String> monthLabels = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.getDefault());

        for (int i = 0; i < 12; i++) {
            calendar.set(Calendar.MONTH, i);
            long[] monthTimestamps = getMonthTimestamps(calendar);

            List<Transaction> monthTransactions = databaseHelper.getFilteredTransactions(
                email, monthTimestamps[0], monthTimestamps[1], null, null, 0, 0, "date", true
            );

            double monthIncome = monthTransactions.stream()
                .filter(t -> t.getType() == Transaction.INCOME_TYPE)
                .mapToDouble(Transaction::getAmount)
                .sum();

            double monthExpense = monthTransactions.stream()
                .filter(t -> t.getType() != Transaction.INCOME_TYPE)
                .mapToDouble(Transaction::getAmount)
                .sum();

            incomeEntries.add(new BarEntry(i, (float)monthIncome));
            expenseEntries.add(new BarEntry(i, (float)monthExpense));
            monthLabels.add(monthFormat.format(calendar.getTime()));
        }

        setupBarChart(incomeEntries, expenseEntries, monthLabels);
    }

    private long[] getMonthTimestamps(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        long startOfMonth = calendar.getTimeInMillis();
        
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        long endOfMonth = calendar.getTimeInMillis();

        return new long[]{startOfMonth, endOfMonth};
    }

    private void setupBarChart(List<BarEntry> incomeEntries, List<BarEntry> expenseEntries, List<String> monthLabels) {
        BarDataSet incomeDataSet = createBarDataSet(incomeEntries, "Income", Color.GREEN);
        BarDataSet expenseDataSet = createBarDataSet(expenseEntries, "Expense", Color.RED);

        BarData barData = new BarData(incomeDataSet, expenseDataSet);
        barData.setBarWidth(0.4f);

        barChartMonthlyTransactions.setData(barData);
        barChartMonthlyTransactions.groupBars(0f, 0.1f, 0f);
        
        configureBarChartXAxis(monthLabels);

        barChartMonthlyTransactions.getDescription().setEnabled(false);
        barChartMonthlyTransactions.animateY(1000);
        barChartMonthlyTransactions.invalidate();
    }

    private BarDataSet createBarDataSet(List<BarEntry> entries, String label, int color) {
        BarDataSet dataSet = new BarDataSet(entries, label);
        dataSet.setColor(color);
        return dataSet;
    }

    private void configureBarChartXAxis(List<String> monthLabels) {
        XAxis xAxis = barChartMonthlyTransactions.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(monthLabels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
    }
}
