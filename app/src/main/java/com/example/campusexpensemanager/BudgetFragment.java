package com.example.campusexpensemanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanager.DatabaseHandler1;
import com.example.campusexpensemanager.Model.Expenses;
import com.example.campusexpensemanager.incomeModel;
import com.example.campusexpensemanager.R;

import java.util.ArrayList;
import java.util.List;

public class BudgetFragment extends Fragment {

    private DatabaseHandler1 databaseHandler1;
    private List<incomeModel> incomeModelList = new ArrayList<>();
    private TextView tvIncome;
    private RecyclerView rvIncome;
    private BudgetAdapter budgetAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);

        init(view);
        fillBudget();

        return view;
    }

    private void init(View view) {
        tvIncome = view.findViewById(R.id.tv_income);
        rvIncome = view.findViewById(R.id.rv_income);
        databaseHandler1 = new DatabaseHandler1(getContext());
    }

    private void fillBudget() {
        incomeModelList = databaseHandler1.getAllIncome();

        long total1 = 0;
        for (incomeModel model : incomeModelList) {
            total1 += Long.parseLong(model.getAmount());
        }
        String totalIncome = String.valueOf(total1);
        tvIncome.setText(  totalIncome +" VND");

        // Initialize adapter with the fragment itself
        budgetAdapter = new BudgetAdapter(getContext(), incomeModelList, databaseHandler1, this);
        rvIncome.setLayoutManager(new LinearLayoutManager(getContext()));
        rvIncome.setHasFixedSize(true);
        rvIncome.setAdapter(budgetAdapter);
    }

    // Method to refresh data in the fragment
    public void refreshData1() {
        loadBudget(); // Reload budget data
    }


    private void loadBudget() {
        incomeModelList.clear();
        incomeModelList.addAll(databaseHandler1.getAllIncome()); // Fetch all expenses from the database
        budgetAdapter.notifyDataSetChanged(); // Notify adapter about data changes
        long total1 = 0;
        for (incomeModel model : incomeModelList) {
            total1 += Long.parseLong(model.getAmount());
        }
        String totalIncome = String.valueOf(total1);
        tvIncome.setText(totalIncome +" VND");
    }
}
