package vn.btec.campusexpensemanagement.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import vn.btec.campusexpensemanagement.DataStatic;
import vn.btec.campusexpensemanagement.R;
import vn.btec.campusexpensemanagement.database.DatabaseHelper;
import vn.btec.campusexpensemanagement.entities.Goals;
import vn.btec.campusexpensemanagement.fragments.GoalFragment;

import java.util.ArrayList;
import java.util.List;

public class AddGoalDialogFragment extends DialogFragment {
    private EditText editTextGoalName, editTextGoalAmount;
    private Spinner spinnerGoalCategory;
    private Button buttonSaveGoal, buttonCancel;
    private DatabaseHelper databaseHelper;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_goal, container, false);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(requireContext());

        // Initialize views
        editTextGoalName = view.findViewById(R.id.editTextGoalName);
        editTextGoalAmount = view.findViewById(R.id.editTextGoalAmount);
        spinnerGoalCategory = view.findViewById(R.id.spinnerGoalCategory);
        buttonSaveGoal = view.findViewById(R.id.buttonSaveGoal);
        buttonCancel = view.findViewById(R.id.buttonCancel);

        // Setup goal categories spinner
        setupGoalCategoriesSpinner();

        // Save goal button click listener
        buttonSaveGoal.setOnClickListener(v -> saveGoal());

        // Cancel button click listener
        buttonCancel.setOnClickListener(v -> dismiss());

        return view;
    }

    private void setupGoalCategoriesSpinner() {
        List<String> categories = new ArrayList<>();
        categories.add("Travel");
        categories.add("Emergency Fund");
        categories.add("Education");
        categories.add("Electronics");
        categories.add("Vehicle");
        categories.add("Home");
        categories.add("Other");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            requireContext(), 
            android.R.layout.simple_spinner_item, 
            categories
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGoalCategory.setAdapter(adapter);
    }

    private void saveGoal() {
        String goalName = editTextGoalName.getText() != null ? editTextGoalName.getText().toString().trim() : "";
        String goalAmountStr = editTextGoalAmount.getText() != null ? editTextGoalAmount.getText().toString().trim() : "";

        if (goalName.isEmpty() || goalAmountStr.isEmpty()) {
            showToast("Please fill in all fields");
            return;
        }

        try {
            double goalAmount = Double.parseDouble(goalAmountStr);
            if (goalAmount <= 0) {
                showToast("Please enter a positive goal amount");
                return;
            }

            Goals newGoal = new Goals();
            newGoal.setGoalName(goalName);
            newGoal.setGoalAmount(goalAmount);
            newGoal.setCurrentAmount(0.0); // Start with 0
            newGoal.setAchieved(false);
            newGoal.setEmail(DataStatic.email); // Ensure DataStatic.email is set

            // Change the variable type to 'long' as 'addGoal' returns a long
            long result = databaseHelper.addGoal(newGoal);
            if (result > 0) { // Check if the insertion was successful
                showToast("Goal added successfully");

                if (getTargetFragment() instanceof GoalFragment) {
                    ((GoalFragment) getTargetFragment()).loadGoalsAndUpdateSavings();
                }

                dismiss();
            } else {
                showToast("Failed to add goal");
            }
        } catch (NumberFormatException e) {
            showToast("Please enter a valid number for the goal amount");
        } catch (Exception e) {
            showToast("An unexpected error occurred: " + e.getMessage());
        }
    }


    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}
