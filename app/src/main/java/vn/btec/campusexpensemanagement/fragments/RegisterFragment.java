package vn.btec.campusexpensemanagement.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import vn.btec.campusexpensemanagement.R;
import vn.btec.campusexpensemanagement.database.DatabaseHelper;

public class RegisterFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private TextInputEditText edFirstName, edLastName, edtEmail, edtPassword, edtConfirmPassword;
    private Button btnRegister;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        initializeComponents(view);
        return view;
    }

    private void initializeComponents(View view) {
        databaseHelper = new DatabaseHelper(requireContext());

        // Initialize EditTexts
        edFirstName = view.findViewById(R.id.edFirstName);
        edLastName = view.findViewById(R.id.edLastName);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtPassword = view.findViewById(R.id.edtPassword);
        edtConfirmPassword = view.findViewById(R.id.edtConfirmPassword);

        // Initialize Register Button
        btnRegister = view.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> performRegistration());
    }

    private void performRegistration() {
        String firstName = edFirstName.getText().toString().trim();
        String lastName = edLastName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        // Validate input fields
        if (TextUtils.isEmpty(firstName)) {
            edFirstName.setError("First name is required");
            return;
        }

        if (TextUtils.isEmpty(lastName)) {
            edLastName.setError("Last name is required");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Password is required");
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            edtConfirmPassword.setError("Confirm password is required");
            return;
        }

        if (!password.equals(confirmPassword)) {
            edtConfirmPassword.setError("Passwords do not match");
            return;
        }

        // Check if email is already registered
        if (databaseHelper.isRegisteredEmailDuplicate(email)) {
            edtEmail.setError("Email is already registered");
            return;
        }

        // Attempt to register
        boolean isRegistered = databaseHelper.signUp(firstName, lastName, email, password);
        if (isRegistered) {
            Toast.makeText(requireContext(), "Registration successful!", Toast.LENGTH_SHORT).show();
            // Clear fields after successful registration
            clearFields();
            // Navigate back to login
            requireActivity().getSupportFragmentManager().popBackStack();
        } else {
            Toast.makeText(requireContext(), "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        edFirstName.setText("");
        edLastName.setText("");
        edtEmail.setText("");
        edtPassword.setText("");
        edtConfirmPassword.setText("");
    }
}
