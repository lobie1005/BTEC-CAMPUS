package vn.btec.campusexpensemanagement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import vn.btec.campusexpensemanagement.R;
import vn.btec.campusexpensemanagement.entities.Goals;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class GoalAdapter extends ArrayAdapter<Goals> {
    private Context context;
    private List<Goals> goalsList;

    public GoalAdapter(@NonNull Context context, @NonNull List<Goals> goals) {
        super(context, R.layout.item_goal, goals);
        this.context = context;
        this.goalsList = goals;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_goal, parent, false);
        }

        // Get the current goal
        Goals currentGoal = goalsList.get(position);

        // Initialize views
        TextView tvGoalName = convertView.findViewById(R.id.tvGoalName);
        TextView tvGoalProgress = convertView.findViewById(R.id.tvGoalProgress);
        ProgressBar progressBar = convertView.findViewById(R.id.progressBarGoal);
        TextView tvGoalAmount = convertView.findViewById(R.id.tvGoalAmount);

        // Format currency
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);

        // Set goal name
        tvGoalName.setText(currentGoal.getGoalName());

        // Calculate and set progress
        double goalAmount = currentGoal.getGoalAmount();
        double currentAmount = currentGoal.getCurrentAmount();
        int progressPercentage = (int) ((currentAmount / goalAmount) * 100);

        // Set progress bar
        progressBar.setMax(100);
        progressBar.setProgress(progressPercentage);

        // Set progress text
        String progressText = progressPercentage + "% (" + 
            currencyFormatter.format(currentAmount) + " / " + 
            currencyFormatter.format(goalAmount) + ")";
        tvGoalProgress.setText(progressText);

        // Set goal amount
        tvGoalAmount.setText(currencyFormatter.format(goalAmount));

        return convertView;
    }

    // Method to update the list
    public void updateGoals(List<Goals> newGoalsList) {
        this.goalsList.clear();
        this.goalsList.addAll(newGoalsList);
        notifyDataSetChanged();
    }
}
