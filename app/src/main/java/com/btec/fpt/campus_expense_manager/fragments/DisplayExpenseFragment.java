package com.btec.fpt.campus_expense_manager.fragments;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.btec.fpt.campus_expense_manager.R;
import com.btec.fpt.campus_expense_manager.database.DatabaseHelper;
import com.btec.fpt.campus_expense_manager.entities.Category;
import com.btec.fpt.campus_expense_manager.entities.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DisplayExpenseFragment extends Fragment {
    private DatabaseHelper dbHelper;
    private ListView expensesListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_expense, container, false);
        
        initializeComponents(view);
        loadExpenses();
        
        return view;
    }

    private void initializeComponents(View view) {
        dbHelper = new DatabaseHelper(requireContext());
        expensesListView = view.findViewById(R.id.expensesListView);
    }

    private void loadExpenses() {
        // Get current user's email
        String currentUserEmail = dbHelper.getCurrentUserEmail();
        
        // Fetch transactions for the current user
        List<Transaction> transactionList = dbHelper.getAllTransactionsByEmail(currentUserEmail);
        
        // Filter only expense transactions
        List<Transaction> expenseTransactions = transactionList.stream()
            .filter(t -> t.getType() == Transaction.EXPENSE_TYPE)
            .collect(Collectors.toList());

        // Extract transaction descriptions
        ArrayList<String> transactionDescriptions = expenseTransactions.stream()
            .map(Transaction::getDescription)
            .collect(Collectors.toCollection(ArrayList::new));

        // Create adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            requireContext(), 
            android.R.layout.simple_list_item_1, 
            transactionDescriptions
        );
        expensesListView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
