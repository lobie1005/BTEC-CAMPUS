package vn.btec.campusexpensemanagement.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import vn.btec.campusexpensemanagement.LoginActivity;
import vn.btec.campusexpensemanagement.R;
import vn.btec.campusexpensemanagement.fragments.*;
import vn.btec.campusexpensemanagement.database.DatabaseHelper;
import vn.btec.campusexpensemanagement.entities.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsFragment extends Fragment {

    private CircleImageView ivProfileAvatar;
    private ImageView ivEditProfilePicture;
    private TextInputEditText etFullName, etEmail;
    private MaterialButton btnLogout;
    private Slider sliderBudgetNotification;
    private TextView tvBudgetNotificationPercent;

    private DatabaseHelper databaseHelper;
    private String currentUserEmail;
    private Integer currentUserId;

    private ActivityResultLauncher<Intent> galleryLauncher;

    private EditText etCurrentPassword, etNewPassword, etConfirmPassword;
    private EditText etBudgetLimit;
    private  Switch switchNotifications;
    private Button btnChangePassword, btnSaveSettings, btnManageCategories;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHelper = new DatabaseHelper(requireContext());
        currentUserEmail = databaseHelper.getCurrentUserEmail();

        // Gallery Image Selection Launcher
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        uploadProfileImage(imageUri);
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        initializeViews(view);
        setupListeners();
        loadUserProfile();

        sharedPreferences = requireContext().getSharedPreferences("settings", requireContext().MODE_PRIVATE);

        etCurrentPassword = view.findViewById(R.id.etCurrentPassword);
        etNewPassword = view.findViewById(R.id.etNewPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        etBudgetLimit = view.findViewById(R.id.etBudgetLimit);
        switchNotifications = view.findViewById(R.id.switchNotifications);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        btnSaveSettings = view.findViewById(R.id.btnSaveSettings);
        btnManageCategories = view.findViewById(R.id.btnManageCategories);

        loadCurrentSettings();
        setupSettingsListeners();

        return view;
    }

    private void initializeViews(View view) {
        ivProfileAvatar = view.findViewById(R.id.ivProfileAvatar);
        ivEditProfilePicture = view.findViewById(R.id.ivEditProfilePicture);
        etFullName = view.findViewById(R.id.etFullName);
        etEmail = view.findViewById(R.id.etEmail);
        btnLogout = view.findViewById(R.id.btnLogout);
        sliderBudgetNotification = view.findViewById(R.id.sliderBudgetNotification);
        tvBudgetNotificationPercent = view.findViewById(R.id.tvBudgetNotificationPercent);
    }

    private void setupListeners() {
        // Profile Picture Edit
        ivEditProfilePicture.setOnClickListener(v -> openGallery());

        // Logout
        btnLogout.setOnClickListener(v -> logout());

        // Budget Notification Slider
        sliderBudgetNotification.addOnChangeListener((slider, value, fromUser) -> {
            tvBudgetNotificationPercent.setText(
                    String.format("Notify when budget reaches %d%%", (int) value));
            saveBudgetNotificationPreference((int) value);
        });

        // Change Information Clicks
        etFullName.setOnClickListener(v -> showChangeNameDialog());
        etEmail.setOnClickListener(v -> showChangeEmailDialog());
        
        // Add password change listener
        View passwordChangeView = getView().findViewById(R.id.changePasswd);
        if (passwordChangeView != null) {
            passwordChangeView.setOnClickListener(v -> showChangePasswordDialog());
        }
    }

    private void loadUserProfile() {
        if (currentUserEmail != null) {
            User currentUser = databaseHelper.getUserByEmail(currentUserEmail);
            if (currentUser != null) {
                // Set full name and email
                etFullName.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
                etEmail.setText(currentUserEmail);

                // Load budget notification preference
                // You might want to add a method in DatabaseHelper to get budget notification percent
                Long budgetNotificationPercent = 50L; // Placeholder
                if (budgetNotificationPercent != null) {
                    sliderBudgetNotification.setValue(budgetNotificationPercent);
                    tvBudgetNotificationPercent.setText(
                            String.format("Notify when budget reaches %d%%", budgetNotificationPercent.intValue()));
                }
            }
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        galleryLauncher.launch(galleryIntent);
    }

    private void uploadProfileImage(Uri imageUri) {
        // TODO: Implement local image upload and storage
        Toast.makeText(requireContext(), "Image upload not implemented", Toast.LENGTH_SHORT).show();
    }

    private void saveBudgetNotificationPreference(int percent) {
        // TODO: Implement saving budget notification preference in local database
        Toast.makeText(requireContext(), "Budget Notification Updated", Toast.LENGTH_SHORT).show();
    }

    private void logout() {
        databaseHelper.clearCurrentUserEmail();
        startActivity(new Intent(requireContext(), LoginActivity.class));
        requireActivity().finish();
    }

    private void showChangeNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Change Name");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_change_name, null);
        TextInputEditText etNewName = dialogView.findViewById(R.id.etNewName);
        
        builder.setView(dialogView);

        // Set current name as default
        User currentUser = databaseHelper.getUserByEmail(currentUserEmail);
        if (currentUser != null) {
            etNewName.setText(currentUser.getId() + "" + currentUser.getFirstName() + " " + currentUser.getLastName());
        }

        builder.setPositiveButton("Update", (dialog, which) -> {
            String newName = etNewName.getText().toString().trim();

            if (TextUtils.isEmpty(newName)) {
                etNewName.setError("Name cannot be empty");
                return;
            }

            // Split name into first and last name
            String[] nameParts = newName.split(" ", 2);
            String firstName = nameParts[0];
            String lastName = nameParts.length > 1 ? nameParts[1] : "";

            // Update user in local database
            boolean updateSuccess = databaseHelper.updateUser(
                currentUser.getId(),
                firstName, 
                lastName, 
                currentUserEmail, 
                currentUser.getPassword()
            );

            if (updateSuccess) {
                // Update UI
                etFullName.setText(newName);
                Toast.makeText(requireContext(), "Name updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Failed to update name", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void showChangeEmailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Change Email");

        View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_change_email, null);
        EditText etOldEmail = view.findViewById(R.id.et_old_email);
        EditText etNewEmail = view.findViewById(R.id.et_new_email);

        builder.setView(view);

        builder.setPositiveButton("Change", (dialog, which) -> {
            String oldEmail = etOldEmail.getText().toString().trim();
            String newEmail = etNewEmail.getText().toString().trim();

            if (TextUtils.isEmpty(oldEmail) || TextUtils.isEmpty(newEmail)) {
                Toast.makeText(requireContext(), "Email cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            // Verify old email matches current email
            if (!oldEmail.equals(currentUserEmail)) {
                Toast.makeText(requireContext(), "Old email is incorrect", Toast.LENGTH_SHORT).show();
                return;
            }

            User currentUser = databaseHelper.getUserByEmail(currentUserEmail);
            if (currentUser != null) {
                // Update user in local database
                boolean updateSuccess = databaseHelper.updateUser(
                    currentUser.getId(),
                    currentUser.getFirstName(), 
                    currentUser.getLastName(), 
                    newEmail, 
                    currentUser.getPassword()
                );

                if (updateSuccess) {
                    // Update SharedPreferences with new email
                    databaseHelper.setCurrentUserEmail(newEmail);
                    currentUserEmail = newEmail;

                    // Update UI
                    etEmail.setText(newEmail);
                    Toast.makeText(requireContext(), "Email updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Failed to update email", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void showChangePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Change Password");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_change_password, null);
        EditText etOldPassword = dialogView.findViewById(R.id.et_old_password);
        EditText etNewPassword = dialogView.findViewById(R.id.et_new_password);
        EditText etConfirmNewPassword = dialogView.findViewById(R.id.et_re_new_password);
        
        builder.setView(dialogView);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String oldPassword = etOldPassword.getText().toString().trim();
            String newPassword = etNewPassword.getText().toString().trim();
            String confirmNewPassword = etConfirmNewPassword.getText().toString().trim();

            // Validate inputs
            if (TextUtils.isEmpty(oldPassword)) {
                etOldPassword.setError("Old password cannot be empty");
                return;
            }
            if (TextUtils.isEmpty(newPassword)) {
                etNewPassword.setError("New password cannot be empty");
                return;
            }
            if (!newPassword.equals(confirmNewPassword)) {
                etConfirmNewPassword.setError("Passwords do not match");
                return;
            }

            // Use DatabaseHelper to change password
            boolean passwordChanged = databaseHelper.changePassword(currentUserEmail, oldPassword, newPassword);

            if (passwordChanged) {
                Toast.makeText(requireContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Failed to update password. Check your old password.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    private void loadCurrentSettings() {
        // Load budget limit
        double budgetLimit = sharedPreferences.getFloat("budget_limit", 0);
        etBudgetLimit.setText(String.valueOf(budgetLimit));

        // Load notification preference
        boolean notificationsEnabled = sharedPreferences.getBoolean("notifications_enabled", true);
        switchNotifications.setChecked(notificationsEnabled);
    }

    private void setupSettingsListeners() {
        btnChangePassword.setOnClickListener(v -> changePassword());
        btnSaveSettings.setOnClickListener(v -> saveSettings());
        btnManageCategories.setOnClickListener(v -> navigateToManageCategories());
    }

    private void changePassword() {
        String currentPassword = etCurrentPassword.getText().toString();
        String newPassword = etNewPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showToast("Please fill in all password fields");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showToast("New passwords do not match");
            return;
        }

        boolean success = databaseHelper.changePassword(currentUserEmail, currentPassword, newPassword);

        if (success) {
            showToast("Password changed successfully");
            clearPasswordFields();
        } else {
            showToast("Failed to change password. Please check your current password");
        }
    }

    private void saveSettings() {
        try {
            float budgetLimit = Float.parseFloat(etBudgetLimit.getText().toString());
            boolean notificationsEnabled = switchNotifications.isChecked();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("budget_limit", budgetLimit);
            editor.putBoolean("notifications_enabled", notificationsEnabled);
            editor.apply();

            showToast("Settings saved successfully");
        } catch (NumberFormatException e) {
            showToast("Please enter a valid budget limit");
        }
    }

    private void navigateToManageCategories() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new ManageCategoriesFragment())
                .addToBackStack(null)
                .commit();
    }

    private void clearPasswordFields() {
        etCurrentPassword.setText("");
        etNewPassword.setText("");
        etConfirmPassword.setText("");
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
