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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.btec.fpt.campus_expense_manager.*;

public class PlaceholderFragment extends Fragment {
    private static final String ARG_IS_INCOME = "is_income";

    private RecyclerView recyclerView;
    private TextView tvEmptyState;
    private CategoryAdapter adapter;
    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private boolean isIncome;

    public static PlaceholderFragment newInstance(boolean isIncome) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_INCOME, isIncome);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isIncome = getArguments().getBoolean(ARG_IS_INCOME);
        }
        databaseHelper = new DatabaseHelper(requireContext());
        sessionManager = new SessionManager(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);
        
        recyclerView = view.findViewById(R.id.recyclerView);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);
        
        setupRecyclerView();
        loadCategories();
        
        return view;
    }

    private void setupRecyclerView() {
        adapter = new CategoryAdapter(isIncome);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        adapter.setOnCategoryDeleteListener(category -> {
            if (category != null) {
                deleteCategory(category);
            }
        });
    }

    public void loadCategories() {
        String userEmail = sessionManager.getUserEmail();
        if (isIncome) {
            List<IncomeCategory> categories = databaseHelper.getIncomeCategories(userEmail);
            adapter.updateCategories(categories);
            updateEmptyState(categories.isEmpty());
        } else {
            List<ExpenseCategory> categories = databaseHelper.getExpenseCategories(userEmail);
            adapter.updateCategories(categories);
            updateEmptyState(categories.isEmpty());
        }
    }

    private void deleteCategory(Object category) {
        String userEmail = sessionManager.getUserEmail();
        boolean success = false;
        
        if (isIncome && category instanceof IncomeCategory) {
            IncomeCategory incomeCategory = (IncomeCategory) category;
            if (!incomeCategory.isDefault()) {
                databaseHelper.deleteIncomeCategory(incomeCategory.getId(), userEmail);
                success = true;
            }
        } else if (!isIncome && category instanceof ExpenseCategory) {
            ExpenseCategory expenseCategory = (ExpenseCategory) category;
            if (!expenseCategory.isDefault()) {
                databaseHelper.deleteExpenseCategory(expenseCategory.getId(), userEmail);
                success = true;
            }
        }

        if (success) {
            Toast.makeText(requireContext(), "Category deleted successfully", Toast.LENGTH_SHORT).show();
            loadCategories(); // Reload the list
        } else {
            Toast.makeText(requireContext(), "Cannot delete default category", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateEmptyState(boolean isEmpty) {
        recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        tvEmptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        
        String emptyText = isIncome ? 
            "Không có danh mục thu nhập nào. Nhấn + để thêm mới." :
            "Không có danh mục chi tiêu nào. Nhấn + để thêm mới.";
        tvEmptyState.setText(emptyText);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCategories(); // Reload categories when fragment becomes visible
    }
}
