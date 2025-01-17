package com.btec.fpt.campus_expense_manager.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.btec.fpt.campus_expense_manager.DataStatic;
import com.btec.fpt.campus_expense_manager.R;
import com.btec.fpt.campus_expense_manager.database.DatabaseHelper;
import com.btec.fpt.campus_expense_manager.entities.Category;
import com.btec.fpt.campus_expense_manager.entities.Transaction;

import java.util.ArrayList;

public class AddExpenseFragment extends Fragment {


    private DatabaseHelper dbHelper;
    private EditText amountEditText, descriptionEditText, dateEditText;


    public AddExpenseFragment(){

    }
    static  String categoryString = "Food";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_add_expense, container, false);

        dbHelper = new DatabaseHelper(getContext());

        amountEditText = view.findViewById(R.id.amountEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        dateEditText = view.findViewById(R.id.dateEditText);


        // Reference to the Spinner in the layout
        Spinner spinner = view.findViewById(R.id.spinner);


        // Extract category names into an ArrayList
        ArrayList<String> categoryNames = new ArrayList<>();
        for (Category category : dbHelper.getAllCategoryByEmail(DataStatic.email)) {
            categoryNames.add(category.getName());
        }

        // Convert ArrayList to an array if required
        String[] categoryNameArray = categoryNames.toArray(new String[0]);


        // Data for the Spinner
       // String[] categories = {"Food", "Transport", "Entertainment", "Health", "Education"};

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                categoryNameArray
        );

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter to the Spinner
        spinner.setAdapter(adapter);

        // Set a listener for when an item is selected
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                categoryString = selectedCategory;

                Toast.makeText(getContext(), "Selected: " + selectedCategory, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optional: Handle case when nothing is selected
            }
        });
        Button addButton = view.findViewById(R.id.addButton);

        Button btnDisplay = view.findViewById(R.id.btnDisplay);

        btnDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadFragment(new DisplayExpenseFragment());


            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExpense();
            }
        });
        return view;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void addExpense() {
        double amount = Double.parseDouble(amountEditText.getText().toString());
        String description = descriptionEditText.getText().toString();
        String date = dateEditText.getText().toString();


// type =0 co nghia la khoan chi tieu expense
        // type 1 la Income thu nhap la tien vao tai khoan
        boolean inserted = dbHelper.insertTransaction(amount, description, date,0, DataStatic.email,categoryString );
        if (inserted) {
            Toast.makeText(getContext(), "Expense added", Toast.LENGTH_SHORT).show();
            amountEditText.setText("");
            descriptionEditText.setText("");
            dateEditText.setText("");



        } else {
            Toast.makeText(getContext(), "Error adding expense", Toast.LENGTH_SHORT).show();
        }
    }
}
