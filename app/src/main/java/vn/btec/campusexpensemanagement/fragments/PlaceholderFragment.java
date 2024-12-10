package vn.btec.campusexpensemanagement.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import vn.btec.campusexpensemanagement.R;
import vn.btec.campusexpensemanagement.adapters.TransactionAdapter;
import vn.btec.campusexpensemanagement.database.DatabaseHelper;
import vn.btec.campusexpensemanagement.entities.Transaction;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PlaceholderFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerViewTransactions;
    private TransactionAdapter transactionAdapter;
    private TabLayout tabLayoutTransactions;
    private ExtendedFloatingActionButton fabAddTransaction;
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_placeholder, container, false);

        databaseHelper = new DatabaseHelper(requireContext());

        initializeViews(view);
        setupTransactionList();
        setupTabLayout();
        setupFabAddTransaction();
        setupToolbar();

        return view;
    }

    private void initializeViews(View view) {
        recyclerViewTransactions = view.findViewById(R.id.recyclerViewTransactions);
        tabLayoutTransactions = view.findViewById(R.id.tabLayoutTransactions);
        fabAddTransaction = view.findViewById(R.id.fabAddTransaction);
        toolbar = view.findViewById(R.id.toolbar);
    }

    private void setupTransactionList() {
        List<Transaction> transactions = databaseHelper.getAllTransactionsByEmail(
            databaseHelper.getCurrentUserEmail()
        );

        transactionAdapter = new TransactionAdapter(transactions);
        recyclerViewTransactions.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewTransactions.setAdapter(transactionAdapter);
    }

    private void setupTabLayout() {
        tabLayoutTransactions.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                filterTransactions(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void filterTransactions(int position) {
        List<Transaction> allTransactions = databaseHelper.getAllTransactionsByEmail(
            databaseHelper.getCurrentUserEmail()
        );

        List<Transaction> filteredTransactions = new ArrayList<>();
        switch (position) {
            case 0: // All
                filteredTransactions = allTransactions;
                break;
            case 1: // Income
                for (Transaction t : allTransactions) {
                    if (t.getType() == Transaction.INCOME_TYPE) {
                        filteredTransactions.add(t);
                    }
                }
                break;
            case 2: // Expense
                for (Transaction t : allTransactions) {
                    if (t.getType() == Transaction.EXPENSE_TYPE) {
                        filteredTransactions.add(t);
                    }
                }
                break;
        }

        transactionAdapter.updateTransactions(filteredTransactions);
    }

    private void setupFabAddTransaction() {
        fabAddTransaction.setOnClickListener(v -> showAddTransactionDialog());
    }

    private void setupToolbar() {
        toolbar.inflateMenu(R.menu.placeholder_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_manage_categories) {
                // Navigate to Manage Categories Fragment
                requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ManageCategoriesFragment())
                    .addToBackStack(null)
                    .commit();
                return true;
            }
            return false;
        });
    }

    private void showAddTransactionDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_add_transaction);

        Spinner spinnerTransactionType = dialog.findViewById(R.id.spinnerTransactionType);
        EditText etAmount = dialog.findViewById(R.id.etAmount);
        EditText etDescription = dialog.findViewById(R.id.etDescription);
        EditText etCategory = dialog.findViewById(R.id.etCategory);
        Button btnSave = dialog.findViewById(R.id.btnSave);

        // Populate transaction type spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
            requireContext(), 
            R.array.transaction_types, 
            android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTransactionType.setAdapter(adapter);

        btnSave.setOnClickListener(v -> {
            String amountStr = etAmount.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String category = etCategory.getText().toString().trim();
            int transactionType = spinnerTransactionType.getSelectedItemPosition() == 0 
                ? Transaction.INCOME_TYPE 
                : Transaction.EXPENSE_TYPE;

            if (validateTransaction(amountStr, description, category)) {
                double amount = Double.parseDouble(amountStr);
                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(new Date());

                databaseHelper.addTransaction(
                    amount, 
                    description, 
                    currentDate, 
                    transactionType, 
                    databaseHelper.getCurrentUserEmail(), 
                    category
                );

                setupTransactionList();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private boolean validateTransaction(String amount, String description, String category) {
        boolean isValid = true;

        if (amount.isEmpty()) {
            Toast.makeText(requireContext(), "Amount cannot be empty", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (description.isEmpty()) {
            Toast.makeText(requireContext(), "Description cannot be empty", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        if (category.isEmpty()) {
            Toast.makeText(requireContext(), "Category cannot be empty", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }
}
