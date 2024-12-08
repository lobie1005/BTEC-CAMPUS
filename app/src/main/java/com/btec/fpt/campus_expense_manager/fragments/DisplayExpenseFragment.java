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

import com.btec.fpt.campus_expense_manager.DataStatic;
import com.btec.fpt.campus_expense_manager.R;
import com.btec.fpt.campus_expense_manager.database.DatabaseHelper;
import com.btec.fpt.campus_expense_manager.entities.Category;
import com.btec.fpt.campus_expense_manager.entities.Transaction;

import java.util.ArrayList;
import java.util.List;

public class DisplayExpenseFragment extends Fragment {
    public DisplayExpenseFragment(){
    }

    private DatabaseHelper dbHelper;
    private ListView expensesListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_expense, container, false);
        dbHelper = new DatabaseHelper(getContext());
        expensesListView = view.findViewById(R.id.expensesListView);
        loadExpenses();
        return view;
    }

    private void loadExpenses() {

        List<Transaction> transactionList = new ArrayList<>();
        transactionList = dbHelper.getAllTransactionsByEmail(DataStatic.email);

        // Extract category names into an ArrayList
        ArrayList<String> transactionName = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            transactionName.add(transaction.getDescription());
        }

        // Convert ArrayList to an array if required
        String[] categoryNameArray = transactionName.toArray(new String[0]);



        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, categoryNameArray);
        expensesListView.setAdapter(adapter);
    }

}
