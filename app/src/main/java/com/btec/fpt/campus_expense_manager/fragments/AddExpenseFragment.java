Lpackage com.btec.fpt.campus_expense_manager.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.btec.fpt.campus_expense_manager.DataStatic;
import com.btec.fpt.campus_expense_manager.R;
import com.btec.fpt.campus_expense_manager.database.DatabaseHelper;
import com.btec.fpt.campus_expense_manager.entities.Category;
import com.btec.fpt.campus_expense_manager.entities.Transaction;
import com.btec.fpt.campus_expense_manager.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class AddExpenseFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private EditText amountEditText, descriptionEditText, dateEditText;
    private Spinner categorySpinner;
    private String selectedCategory;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_expense, container, false);

        initializeComponents(view);
        setupCategorySpinner();
        setupButtonListeners(view);

        return view;
    }

    private void initializeComponents(View view) {
        dbHelper = new DatabaseHelper(requireContext());

        amountEditText = view.findViewById(R.id.amountEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        dateEditText = view.findViewById(R.id.dateEditText);
        categorySpinner = view.findViewById(R.id.spinner);
    }

    private void setupCategorySpinner() {
        List<Category> categories = dbHelper.getAllCategoryByEmail(DataStatic.email);
        List<String> categoryNames = new ArrayList<>();

        for (Category category : categories) {
            categoryNames.add(category.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            categoryNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = null;
            }
        });
    }

    private void setupButtonListeners(View view) {
        Button addButton = view.findViewById(R.id.addButton);
        Button btnDisplay = view.findViewById(R.id.btnDisplay);

        addButton.setOnClickListener(v -> addExpenseTransaction());
        btnDisplay.setOnClickListener(v -> navigateToDisplayExpenseFragment());
    }

    private void addExpenseTransaction() {
        String amountStr = amountEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String dateStr = dateEditText.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();

        // Validate input
        if (!validateInput(amountStr, description, dateStr)) {
            return;
        }

        // Parse amount and date
        double amount = Double.parseDouble(amountStr);
        long timestamp = DateUtils.parseDate(dateStr);

        // Create transaction
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setCategory(category);
        transaction.setDate(timestamp);
        transaction.setType(Transaction.EXPENSE_TYPE);
        
        // Set the current user's email
        String currentUserEmail = dbHelper.getCurrentUserEmail();
        if (TextUtils.isEmpty(currentUserEmail)) {
            showToast("User not logged in");
            return;
        }
        transaction.setEmail(currentUserEmail);

        // Save transaction
        long result = dbHelper.addTransaction(transaction);

        // Handle result
        if (result != -1) {
            showToast("Expense Added Successfully");
            clearInputFields();
            navigateToDisplayExpenseFragment();
        } else {
            showToast("Failed to Add Expense");
        }
    }

    private boolean validateInput(String amountStr, String description, String dateStr) {
        if (TextUtils.isEmpty(amountStr)) {
            showToast("Amount cannot be empty");
            return false;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                showToast("Amount must be greater than zero");
                return false;
            }
        } catch (NumberFormatException e) {
            showToast("Invalid amount format");
            return false;
        }

        if (TextUtils.isEmpty(description)) {
            showToast("Description cannot be empty");
            return false;
        }

        if (TextUtils.isEmpty(dateStr)) {
            showToast("Date cannot be empty");
            return false;
        }

        if (selectedCategory == null) {
            showToast("Please select a category");
            return false;
        }

        return true;
    }

    private void navigateToDisplayExpenseFragment() {
        Fragment displayExpenseFragment = new DisplayExpenseFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, displayExpenseFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void clearInputFields() {
        amountEditText.setText("");
        descriptionEditText.setText("");
        dateEditText.setText("");
        categorySpinner.setSelection(0);
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
