package com.example.campusexpensemanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusexpensemanager.Adapter.budgetAdapter2;
import com.example.campusexpensemanager.Adapter.expenseAdapter;
import com.example.campusexpensemanager.DataBase.ExpensesData;
import com.example.campusexpensemanager.Model.Expenses;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView rv_income, rv_expense;
    private TextView tv_income, tv_expense, tv_warning; // Thêm tv_warning
    private Button btnSetLimit;

    private expenseAdapter expenseAdapter;
    private budgetAdapter2 budgetAdapter2;

    private ExpensesData expensesData;

    private List<incomeModel> incomeModelList = new ArrayList<>();
    private DatabaseHandler1 databaseHandler1;

    private String totalIncome, totalExpense;

    private int spendingLimit = 0; // Biến để lưu giới hạn chi tiêu
    private static final String PREFS_NAME = "ExpensePrefs";
    private static final String SPENDING_LIMIT_KEY = "SpendingLimit";

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        init(view);
        expensesData = new ExpensesData(getContext());
        databaseHandler1 = new DatabaseHandler1(getContext());

        fillExpenseModel();
        fillIncomeModel();

        return view;
    }

    private void init(View view) {
        rv_income = view.findViewById(R.id.rv_income);
        rv_expense = view.findViewById(R.id.rv_expense);
        tv_income = view.findViewById(R.id.tv_income);
        tv_expense = view.findViewById(R.id.tv_expense);
        tv_warning = view.findViewById(R.id.tv_warning); // Liên kết tv_warning
        btnSetLimit = view.findViewById(R.id.btn_set_limit);

        // Lấy giới hạn chi tiêu từ SharedPreferences
        SharedPreferences prefs = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        spendingLimit = prefs.getInt(SPENDING_LIMIT_KEY, 0);

        // Xử lý sự kiện click của nút
        btnSetLimit.setOnClickListener(v -> showSetLimitDialog());
    }

    private void fillExpenseModel() {
        List<Expenses> expensesList = expensesData.getAllIncome();
        int total = 0;
        for (Expenses model : expensesList) {
            total += Integer.parseInt(model.getAmount());
        }
        totalExpense = String.valueOf(total);
        tv_expense.setText(totalExpense + " VND");

        // Kiểm tra nếu chi tiêu vượt quá giới hạn
        if (spendingLimit > 0 && total > spendingLimit) {
            // Hiển thị dòng chữ cảnh báo trên giao diện
            tv_warning.setVisibility(View.VISIBLE);
            tv_warning.setText("Your spending has exceeded the limit of " + spendingLimit + " VND!");

            // Hiển thị thông báo cảnh báo qua AlertDialog
//            showWarningDialog("Your spending has exceeded the limit of " + spendingLimit + " VND!");

            // Hiển thị thông báo ngắn qua Toast
            Toast.makeText(getContext(), "Warning: Spending exceeded the limit!", Toast.LENGTH_SHORT).show();
        } else {
            tv_warning.setVisibility(View.GONE); // Ẩn dòng chữ nếu không vượt hạn mức
        }

        expenseAdapter = new expenseAdapter(getContext(), expensesList);
        rv_expense.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_expense.setHasFixedSize(true);
        rv_expense.setAdapter(expenseAdapter);
    }

    private void fillIncomeModel() {
        incomeModelList = databaseHandler1.getAllIncome();

        int total = 0;
        for (incomeModel model : incomeModelList) {
            total += Integer.parseInt(model.getAmount());
        }
        totalIncome = String.valueOf(total);
        tv_income.setText(totalIncome + " VND");

        budgetAdapter2 = new budgetAdapter2(getContext(), incomeModelList);
        rv_income.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_income.setHasFixedSize(true);

        rv_income.setAdapter(budgetAdapter2);
    }

//    private void showWarningDialog(String message) {
//        new AlertDialog.Builder(getContext())
//                .setTitle("Spending Alert")
//                .setMessage(message)
//                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
//                .show();
//    }

    private void showSetLimitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Set Spending Limit");

        // Tạo EditText để nhập giới hạn
        final EditText input = new EditText(getContext());
        input.setHint("Enter limit (VND)");
        builder.setView(input);

        // Nút "OK"
        builder.setPositiveButton("OK", (dialog, which) -> {
            String limitStr = input.getText().toString().trim();
            if (!limitStr.isEmpty()) {
                spendingLimit = Integer.parseInt(limitStr);

                // Lưu giới hạn vào SharedPreferences
                SharedPreferences prefs = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(SPENDING_LIMIT_KEY, spendingLimit);
                editor.apply();

                Toast.makeText(getContext(), "Spending limit set to: " + spendingLimit + " VND", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Please enter a valid limit", Toast.LENGTH_SHORT).show();
            }
        });

        // Nút "Cancel"
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }
}