package com.btec.fpt.campus_expense_manager.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.btec.fpt.campus_expense_manager.R;
import com.btec.fpt.campus_expense_manager.database.DatabaseHelper;
import com.btec.fpt.campus_expense_manager.utils.ValidationUtils;

public class RegisterFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private EditText edtFirstName, edtLastName, edtEmail, edtPassword, edtConfirmPassword;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        initializeComponents(view);
        setupListeners();

        return view;
    }

    private void initializeComponents(View view) {
        databaseHelper = new DatabaseHelper(requireContext());

        edtFirstName = view.findViewById(R.id.firstName);
        edtLastName = view.findViewById(R.id.lastName);
        edtEmail = view.findViewById(R.id.email);
        edtPassword = view.findViewById(R.id.password);
        edtConfirmPassword = view.findViewById(R.id.confirmPassword);
    }

    private void setupListeners() {
        TextView tvLogin = requireView().findViewById(R.id.tvLogin);
        tvLogin.setOnClickListener(v -> navigateToLogin());

        Button buttonSave = requireView().findViewById(R.id.register_button);
        buttonSave.setOnClickListener(v -> performRegistration());
    }

    private void performRegistration() {
        String firstName = edtFirstName.getText().toString().trim();
        String lastName = edtLastName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        if (validateRegistrationInput(firstName, lastName, email, password, confirmPassword)) {
            boolean registrationSuccess = databaseHelper.signUp(firstName, lastName, email, password);

            if (registrationSuccess) {
                showToast("Register successfully");
                navigateToLogin();
            } else {
                showToast("Cannot register. Try again.");
            }
        }
    }

    private boolean validateRegistrationInput(String firstName, String lastName, String email, 
                                              String password, String confirmPassword) {
        if (TextUtils.isEmpty(firstName)) {
            showToast("First name cannot be empty");
            return false;
        }

        if (TextUtils.isEmpty(lastName)) {
            showToast("Last name cannot be empty");
            return false;
        }

        if (!ValidationUtils.isValidEmail(email)) {
            showToast("Invalid email format");
            return false;
        }

        if (!ValidationUtils.isValidPassword(password)) {
            showToast("Password must be at least 8 characters with uppercase, lowercase, and number");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showToast("Passwords do not match");
            return false;
        }

        return true;
    }

    private void navigateToLogin() {
        Fragment loginFragment = new LoginFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, loginFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
