<<<<<<< Updated upstream:app/src/main/java/vn/btec/campusexpensemanagement/fragments/AddIncomeFragment.java
package vn.btec.campusexpensemanagement.fragments;
=======
package com.btec.fpt.campus_expense_manager.fragments;
>>>>>>> Stashed changes:app/src/main/java/com/btec/fpt/campus_expense_manager/fragments/AddIncomeFragment.java

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

<<<<<<< Updated upstream:app/src/main/java/vn/btec/campusexpensemanagement/fragments/AddIncomeFragment.java
import vn.btec.campusexpensemanagement.R;
import vn.btec.campusexpensemanagement.database.DatabaseHelper;
import vn.btec.campusexpensemanagement.entities.Transaction;
import vn.btec.campusexpensemanagement.utils.DateUtils;
import vn.btec.campusexpensemanagement.utils.ValidationUtils;

import java.util.Calendar;
import java.util.Date;
=======
import com.btec.fpt.campus_expense_manager.R;
import com.btec.fpt.campus_expense_manager.database.DatabaseHelper;
import com.btec.fpt.campus_expense_manager.entities.Transaction;
import com.btec.fpt.campus_expense_manager.utils.DateUtils;
import com.btec.fpt.campus_expense_manager.utils.ValidationUtils;

import java.util.Calendar;
>>>>>>> Stashed changes:app/src/main/java/com/btec/fpt/campus_expense_manager/fragments/AddIncomeFragment.java
import java.util.Locale;

public class AddIncomeFragment extends Fragment {
    private EditText etAmount, etDescription, etDate;
    private Spinner spinnerIncomeCategory;
    private Button btnAddIncome, btnCancel;
    private DatabaseHelper databaseHelper;

    // Income categories
    private static final String[] INCOME_CATEGORIES = {
        "Salary", "Freelance", "Investment", "Bonus", 
        "Rental Income", "Side Hustle", "Other"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_income, container, false);
        
        initializeComponents(view);
        setupSpinners();
        setupListeners();
        
        return view;
    }

    private void initializeComponents(View view) {
        databaseHelper = new DatabaseHelper(requireContext());

        etAmount = view.findViewById(R.id.etIncomeAmount);
        etDescription = view.findViewById(R.id.etIncomeDescription);
        etDate = view.findViewById(R.id.etIncomeDate);
        spinnerIncomeCategory = view.findViewById(R.id.spinnerIncomeCategory);
        btnAddIncome = view.findViewById(R.id.btnAddIncome);
        btnCancel = view.findViewById(R.id.btnCancelIncome);

        // Set current date by default
        etDate.setText(DateUtils.getCurrentDate());
    }

    private void setupSpinners() {
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
            requireContext(), 
            android.R.layout.simple_spinner_item, 
            INCOME_CATEGORIES
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIncomeCategory.setAdapter(categoryAdapter);
    }

    private void setupListeners() {
        // Date picker dialog
        etDate.setOnClickListener(v -> showDatePickerDialog());

        // Add income button
        btnAddIncome.setOnClickListener(v -> addIncome());

        // Cancel button
        btnCancel.setOnClickListener(v -> navigateBack());
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
            requireContext(), 
            this::onDateSet, 
            year, month, day
        );
        datePickerDialog.show();
    }

    private void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, month, dayOfMonth);
        etDate.setText(DateUtils.formatDate(selectedDate.getTimeInMillis()));
    }

    private void addIncome() {
        // Validate inputs
        if (!validateInputs()) {
            return;
        }

        // Create transaction
        Transaction transaction = createIncomeTransaction();

        // Save to database
        long result = databaseHelper.addTransaction(transaction);

        // Handle result
        if (result != -1) {
            Toast.makeText(requireContext(), "Income Added Successfully", Toast.LENGTH_SHORT).show();
            navigateBack();
        } else {
            Toast.makeText(requireContext(), "Failed to Add Income", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs() {
        String amountStr = etAmount.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String dateStr = etDate.getText().toString().trim();

        // Validate amount
        if (!ValidationUtils.isValidAmount(amountStr)) {
            etAmount.setError("Invalid amount");
            etAmount.requestFocus();
            return false;
        }

        // Validate description
        if (!ValidationUtils.isValidDescription(description)) {
            etDescription.setError("Description is required");
            etDescription.requestFocus();
            return false;
        }

        // Validate date
        if (!ValidationUtils.isValidDate(dateStr)) {
            etDate.setError("Invalid date");
            etDate.requestFocus();
            return false;
        }

        return true;
    }

    private Transaction createIncomeTransaction() {
        String amountStr = etAmount.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String dateStr = etDate.getText().toString().trim();
        String category = spinnerIncomeCategory.getSelectedItem().toString();

        double amount = Double.parseDouble(amountStr);
        long timestamp = DateUtils.parseDate(dateStr);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setCategory(category);
<<<<<<< Updated upstream:app/src/main/java/vn/btec/campusexpensemanagement/fragments/AddIncomeFragment.java
        transaction.setDate(String.valueOf(timestamp));
=======
        transaction.setDate(timestamp);
>>>>>>> Stashed changes:app/src/main/java/com/btec/fpt/campus_expense_manager/fragments/AddIncomeFragment.java
        transaction.setType(Transaction.INCOME_TYPE);
        transaction.setEmail(databaseHelper.getCurrentUserEmail());

        return transaction;
    }

    private void navigateBack() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}
