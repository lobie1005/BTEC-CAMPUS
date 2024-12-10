package vn.btec.campusexpensemanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import vn.btec.campusexpensemanagement.database.DatabaseHelper;
import vn.btec.campusexpensemanagement.fragments.DashboardFragment;
import vn.btec.campusexpensemanagement.fragments.ForgotPasswordFragment;
import vn.btec.campusexpensemanagement.fragments.RegisterFragment;
import vn.btec.campusexpensemanagement.utils.ValidationUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    private EditText edtEmail, edtPassword;
    private Button loginButton;
    private TextView registerButton, forgotPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeComponents();
        setupListeners();

        // Check for intent extra to navigate to RegisterFragment
        if (getIntent().getStringExtra("fragment") != null && getIntent().getStringExtra("fragment").equals("register")) {
            navigateToRegister();
        }
    }

    private void initializeComponents() {
        // Initialize your components after calling setContentView
        databaseHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        edtEmail = findViewById(R.id.etEmail); // Ensure these IDs match your XML
        edtPassword = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        registerButton = findViewById(R.id.goto_register_button); // Correct IDs
        forgotPasswordButton = findViewById(R.id.forgot_password_button);
    }

    private void setupListeners() {
        loginButton.setOnClickListener(v -> performLogin());

        // Ensure buttons are not null before setting listeners
        if (registerButton != null) {
            registerButton.setOnClickListener(v -> navigateToRegister());
        }
        if (forgotPasswordButton != null) {
            forgotPasswordButton.setOnClickListener(v -> navigateToForgotPassword());
        }
    }

    private void performLogin() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (validateLoginInput(email, password)) {
            if (authenticateUser(email, password)) {
                // Get user ID first
                int userId = databaseHelper.getUserId(email);
                if (userId != -1) {
                    // Save user information to DataStatic
                    DataStatic.email = email;
                    DataStatic.isLogin = true;
                    DataStatic.userId = userId;

                    saveUserCredentials(email, userId);
                    navigateToHome();
                } else {
                    showToast("Error retrieving user information");
                }
            } else {
                showToast("Email or password incorrect!");
            }
        }
    }

    private boolean validateLoginInput(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            showToast("Email cannot be empty");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            showToast("Password cannot be empty");
            return false;
        }
        if (!ValidationUtils.isValidEmail(email)) {
            showToast("Invalid email format");
            return false;
        }
        return true;
    }

    private boolean authenticateUser(String email, String password) {
        String hashedPassword = sha256(password); // Hash the password using SHA-256
        return databaseHelper.signIn(email, hashedPassword); // Use the hashed password for authentication
    }

    private String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveUserCredentials(String email, int userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.putInt("userId", userId);
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }

    private void navigateToHome() {
        // Start MainActivity after login success
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); // Close LoginActivity to prevent returning back
    }

    private void navigateToRegister() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setReorderingAllowed(true);
        RegisterFragment registerFragment = new RegisterFragment();
        fragmentTransaction.replace(R.id.fragment_container, registerFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void navigateToForgotPassword() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setReorderingAllowed(true);
        ForgotPasswordFragment forgotPasswordFragment = new ForgotPasswordFragment();
        fragmentTransaction.replace(R.id.fragment_container, forgotPasswordFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
