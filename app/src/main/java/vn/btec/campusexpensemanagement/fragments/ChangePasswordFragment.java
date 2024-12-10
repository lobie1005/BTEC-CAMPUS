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
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import vn.btec.campusexpensemanagement.R;
import vn.btec.campusexpensemanagement.database.DatabaseHelper;
import vn.btec.campusexpensemanagement.utils.SharedPreferencesManager;

public class ChangePasswordFragment extends Fragment {
    private TextInputLayout tilCurrentPassword, tilNewPassword, tilConfirmPassword;
    private TextInputEditText edCurrentPassword, edNewPassword, edConfirmPassword;
    private MaterialButton btnChangePassword;
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
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        initializeViews(view);
        setListeners();
        return view;
    }

    private void initializeViews(View view) {
        tilCurrentPassword = view.findViewById(R.id.tilCurrentPassword);
        tilNewPassword = view.findViewById(R.id.tilNewPassword);
        tilConfirmPassword = view.findViewById(R.id.tilConfirmPassword);
        
        edCurrentPassword = view.findViewById(R.id.edCurrentPassword);
        edNewPassword = view.findViewById(R.id.edNewPassword);
        edConfirmPassword = view.findViewById(R.id.edConfirmPassword);
        
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
    }

    private void setListeners() {
        btnChangePassword.setOnClickListener(v -> validateAndChangePassword());

        // Clear errors on focus
        edCurrentPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) tilCurrentPassword.setError(null);
        });
        edNewPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) tilNewPassword.setError(null);
        });
        edConfirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) tilConfirmPassword.setError(null);
        });
    }

    private void validateAndChangePassword() {
        String currentPassword = edCurrentPassword.getText().toString().trim();
        String newPassword = edNewPassword.getText().toString().trim();
        String confirmPassword = edConfirmPassword.getText().toString().trim();
        boolean isValid = true;

        // Validate current password
        if (TextUtils.isEmpty(currentPassword)) {
            tilCurrentPassword.setError("Current password is required");
            isValid = false;
        }

        // Validate new password
        if (TextUtils.isEmpty(newPassword)) {
            tilNewPassword.setError("New password is required");
            isValid = false;
        } else if (newPassword.length() < 6) {
            tilNewPassword.setError("Password must be at least 6 characters");
            isValid = false;
        }

        // Validate confirm password
        if (TextUtils.isEmpty(confirmPassword)) {
            tilConfirmPassword.setError("Please confirm your new password");
            isValid = false;
        } else if (!confirmPassword.equals(newPassword)) {
            tilConfirmPassword.setError("Passwords do not match");
            isValid = false;
        }

        if (isValid) {
            String currentEmail = preferencesManager.getCurrentUser();
            if (databaseHelper.changePassword(currentEmail, currentPassword, newPassword)) {
                Toast.makeText(requireContext(), "Password changed successfully!", Toast.LENGTH_SHORT).show();
                // Update remembered password if Remember Me is enabled
                if (preferencesManager.isRememberMeEnabled()) {
                    preferencesManager.saveLoginCredentials(currentEmail, newPassword);
                }
                Navigation.findNavController(requireView()).navigateUp();
            } else {
                tilCurrentPassword.setError("Current password is incorrect");
            }
        }
    }
}
