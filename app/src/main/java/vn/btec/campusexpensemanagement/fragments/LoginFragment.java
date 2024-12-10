package vn.btec.campusexpensemanagement.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
import vn.btec.campusexpensemanagement.models.User;
import vn.btec.campusexpensemanagement.utils.SharedPreferencesManager;

public class LoginFragment extends Fragment {
    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText edEmail, edPassword;
    private MaterialButton btnLogin;
    private CheckBox cbRememberMe;
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initializeViews(view);
        setListeners();
        checkRememberedCredentials();
        return view;
    }

    private void initializeViews(View view) {
        tilEmail = view.findViewById(R.id.tilEmail);
        tilPassword = view.findViewById(R.id.tilPassword);
        edEmail = view.findViewById(R.id.email);
        edPassword = view.findViewById(R.id.password);
        btnLogin = view.findViewById(R.id.login_button);
        cbRememberMe = view.findViewById(R.id.cbRememberMe);

        view.findViewById(R.id.goto_register_button).setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), RegisterFragment.class);
            startActivity(intent);
        });

        view.findViewById(R.id.goto_forgot_password).setOnClickListener(v ->
                Toast.makeText(requireContext(), "Forgot password feature coming soon!", Toast.LENGTH_SHORT).show());
    }

    private void setListeners() {
        btnLogin.setOnClickListener(v -> validateAndLogin());

        // Clear errors on text change
        edEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) tilEmail.setError(null);
        });
        edPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) tilPassword.setError(null);
        });
    }

    private void checkRememberedCredentials() {
        if (preferencesManager.isRememberMeEnabled()) {
            edEmail.setText(preferencesManager.getRememberedEmail());
            edPassword.setText(preferencesManager.getRememberedPassword());
            cbRememberMe.setChecked(true);
        }
    }

    private void validateAndLogin() {
        String email = edEmail.getText().toString().trim();
        String password = edPassword.getText().toString().trim();
        boolean isValid = true;

        // Validate email
        if (TextUtils.isEmpty(email)) {
            tilEmail.setError("Email is required");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Please enter a valid email address");
            isValid = false;
        }

        // Validate password
        if (TextUtils.isEmpty(password)) {
            tilPassword.setError("Password is required");
            isValid = false;
        }

        if (isValid) {
            User user = databaseHelper.getUserByEmail(email);
            if (user != null) {
                // Assuming the password is hashed in the database
                if (user.getPassword().equals(hashPassword(password))) { // Hash the input password
                    handleSuccessfulLogin(email, password);
                } else {
                    Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Method to hash the password (implement this according to your hashing logic)
    private String hashPassword(String password) {
        // Implement your hashing logic here
        return password; // Placeholder, replace with actual hashing
    }

    private void handleSuccessfulLogin(String email, String password) {
        // Save credentials if remember me is checked
        if (cbRememberMe.isChecked()) {
            preferencesManager.saveLoginCredentials(email, password);
        } else {
            preferencesManager.clearLoginCredentials();
        }

        // Save current user
        preferencesManager.saveCurrentUser(email);

        // Navigate to main screen
        Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_homeFragment);
        Toast.makeText(requireContext(), "Login successful!", Toast.LENGTH_SHORT).show();
    }

    private void clearFields() {
        edEmail.setText("");
        edPassword.setText("");
        tilEmail.setError(null);
        tilPassword.setError(null);
        cbRememberMe.setChecked(false);
    }
}
