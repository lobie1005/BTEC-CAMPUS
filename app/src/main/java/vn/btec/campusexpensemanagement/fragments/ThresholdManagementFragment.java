package vn.btec.campusexpensemanagement.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import vn.btec.campusexpensemanagement.R;
import vn.btec.campusexpensemanagement.database.DatabaseHelper;
import vn.btec.campusexpensemanagement.utils.SharedPreferencesManager;

public class ThresholdManagementFragment extends Fragment {
    private TextInputLayout tilThreshold;
    private TextInputEditText edThreshold;
    private MaterialButton btnSave;
    private DatabaseHelper databaseHelper;
    private SharedPreferencesManager preferencesManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(requireContext());
        preferencesManager = new SharedPreferencesManager(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_threshold_management, container, false);
        initializeViews(view);
        loadCurrentThreshold();
        return view;
    }

    private void initializeViews(View view) {
        tilThreshold = view.findViewById(R.id.tilThreshold);
        edThreshold = view.findViewById(R.id.edThreshold);
        btnSave = view.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> saveThreshold());
    }

    private void loadCurrentThreshold() {
        double currentThreshold = preferencesManager.getSpendingThreshold();
        edThreshold.setText(String.valueOf(currentThreshold));
    }

    private void saveThreshold() {
        String thresholdStr = edThreshold.getText().toString().trim();
        if (TextUtils.isEmpty(thresholdStr)) {
            tilThreshold.setError("Threshold is required");
            return;
        }

        double threshold = Double.parseDouble(thresholdStr);
        preferencesManager.setSpendingThreshold(threshold);
        Toast.makeText(requireContext(), "Threshold saved successfully", Toast.LENGTH_SHORT).show();
    }
}
