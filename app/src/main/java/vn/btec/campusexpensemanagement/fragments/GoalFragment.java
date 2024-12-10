package vn.btec.campusexpensemanagement.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import vn.btec.campusexpensemanagement.R;
import vn.btec.campusexpensemanagement.database.DatabaseHelper;
import vn.btec.campusexpensemanagement.entities.Goals;
import vn.btec.campusexpensemanagement.utils.CurrencyUtils;

import java.util.List;

public class GoalFragment extends Fragment {
    private TextView tvTotalSavings;
    private Button btnAddGoal;
    private DatabaseHelper databaseHelper;
    private List<Goals> goalsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goals, container, false);

        initializeComponents(view);
        setupListeners();
        loadGoalsAndUpdateSavings();

        return view;
    }

    private void initializeComponents(View view) {
        databaseHelper = new DatabaseHelper(requireContext());
        tvTotalSavings = view.findViewById(R.id.tvTotalSavings);
        btnAddGoal = view.findViewById(R.id.btnAddGoal);
    }

    private void setupListeners() {
        btnAddGoal.setOnClickListener(v -> openAddGoalDialog());
    }

    public void loadGoalsAndUpdateSavings() {
        goalsList = databaseHelper.getAllGoals();
        double totalSavings = calculateTotalSavings();
        updateTotalSavingsDisplay(totalSavings);
    }

    private double calculateTotalSavings() {
        return goalsList != null 
            ? goalsList.stream().mapToDouble(Goals::getCurrentAmount).sum() 
            : 0.0;
    }

    private void updateTotalSavingsDisplay(double totalSavings) {
        tvTotalSavings.setText(CurrencyUtils.formatCurrency(totalSavings));
    }

    private void openAddGoalDialog() {
        AddGoalDialogFragment dialogFragment = new AddGoalDialogFragment();
        dialogFragment.setTargetFragment(this, 300);
        
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(dialogFragment, "add_goal_dialog");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}
