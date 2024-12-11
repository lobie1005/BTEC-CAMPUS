package com.example.campusexpensemanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.campusexpensemanager.Adapter.expenseAdapter2;
import com.example.campusexpensemanager.DataBase.ExpensesData;
import com.example.campusexpensemanager.Model.Expenses;

import java.util.ArrayList;
import java.util.List;

public class ExpenseFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private TextView tvExpense;
    private RecyclerView rvExpense;
    private List<Expenses> expenseModelList = new ArrayList<>();
    private ExpensesData expensesData;
    private String totalExpense;

    private ImageView iv_expensePie;

    private expenseAdapter2 expenseAdapter;

    public ExpenseFragment() {
        // Required empty public constructor
    }

    public static ExpenseFragment newInstance(String param1, String param2) {
        ExpenseFragment fragment = new ExpenseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense, container, false);

        init(view);

       iv_expensePie.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(getContext(), PieChart.class));
           }
       });

        fillExpense();
        return view;
    }

    private void fillExpense() {
        expenseModelList = expensesData.getAllIncome();

        int total = 0;
        for (Expenses model : expenseModelList) {
            total += Integer.parseInt(model.getAmount());
        }
        totalExpense = String.valueOf(total);
        tvExpense.setText( totalExpense +" VND");

        // Initialize the adapter with this fragment
        expenseAdapter = new expenseAdapter2(getContext(), expenseModelList, expensesData, this);
        rvExpense.setLayoutManager(new LinearLayoutManager(getContext()));
        rvExpense.setHasFixedSize(true);
        rvExpense.setAdapter(expenseAdapter);
    }

    private void init(View view) {
        tvExpense = view.findViewById(R.id.tvExpense);
        rvExpense = view.findViewById(R.id.rvExpense);
        iv_expensePie = view.findViewById(R.id.iv_expensePie);
        expensesData = new ExpensesData(getContext());
    }

    public void refreshData() {
        loadExpenses(); // Reload expenses data
    }

    private void loadExpenses() {
        expenseModelList.clear();
        expenseModelList.addAll(expensesData.getAllIncome()); // Fetch all expenses from the database
        expenseAdapter.notifyDataSetChanged(); // Notify adapter about data changes
        int total = 0;
        for (Expenses model : expenseModelList) {
            total += Integer.parseInt(model.getAmount());
        }
        totalExpense = String.valueOf(total);
        tvExpense.setText(totalExpense +" VND");
    }
}
