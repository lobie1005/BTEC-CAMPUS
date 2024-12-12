package com.btec.fpt.campus_expense_manager.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.btec.fpt.campus_expense_manager.HomeActivity;
import com.btec.fpt.campus_expense_manager.R;
import com.btec.fpt.campus_expense_manager.database.DatabaseHelper;
import com.btec.fpt.campus_expense_manager.utils.ValidationUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.UUID;

public class LoginFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    private EditText edtEmail, edtPassword;
    private Button loginButton;
    private TextView registerButton, forgotPasswordButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        
        initializeComponents(view);
        setupListeners();
        
        return view;
    }

    private void initializeComponents(View view) {
        databaseHelper = new DatabaseHelper(requireContext());
        
        try {
            MasterKey masterKey = new MasterKey.Builder(requireContext())
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();

            sharedPreferences = EncryptedSharedPreferences.create(
                requireContext(),
                "UserCredentials",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            showToast("Error initializing secure storage");
            e.printStackTrace();
        }
        
        edtEmail = view.findViewById(R.id.email);
        edtPassword = view.findViewById(R.id.password);
        loginButton = view.findViewById(R.id.login_button);
        registerButton = view.findViewById(R.id.goto_register_button);
        forgotPasswordButton = view.findViewById(R.id.goto_forgot_password);
    }

    private void setupListeners() {
        loginButton.setOnClickListener(v -> performLogin());
        registerButton.setOnClickListener(v -> navigateToRegister());
        forgotPasswordButton.setOnClickListener(v -> navigateToForgotPassword());
    }

    private void performLogin() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (validateLoginInput(email, password)) {
            String userUuid = authenticateUser(email, password);
            if (userUuid != null) {
                saveUserCredentials(email, userUuid);
                navigateToHome();
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

    private String authenticateUser(String email, String password) {
        return databaseHelper.signIn(email, password);
    }

    private void saveUserCredentials(String email, String userUuid) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.putString("user_uuid", userUuid);
        editor.apply();
    }

    private void navigateToHome() {
        Intent intent = new Intent(requireActivity(), HomeActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    private void navigateToRegister() {
        // TODO: Implement navigation to RegisterFragment
    }

    private void navigateToForgotPassword() {
        // TODO: Implement navigation to ForgotPasswordFragment
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
