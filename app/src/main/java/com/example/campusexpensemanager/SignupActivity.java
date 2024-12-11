package com.example.campusexpensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusexpensemanager.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);

        binding.signupButton.setOnClickListener(v -> {
            String email = binding.signupEmail.getText().toString().trim();
            String password = binding.signupPassword.getText().toString().trim();
            String confirm = binding.signupConfirm.getText().toString().trim();
            String name = binding.signupName.getText().toString().trim();
            String ageStr = binding.signupAge.getText().toString().trim();
            String phone = binding.signupPhone.getText().toString().trim();

            if (validateInputs(email, password, confirm, name, ageStr, phone)) {
                int age;
                try {
                    age = Integer.parseInt(ageStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(SignupActivity.this, "Invalid age", Toast.LENGTH_SHORT).show();
                    return;
                }
    //kiem tra email da duoc dang ky chua
                if (databaseHelper.checkEmail(email)) {
                    Toast.makeText(SignupActivity.this, "User already exists, please login", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        boolean insert = databaseHelper.insertData(email, password, name, age, phone);
                        if (insert) {
                            Toast.makeText(SignupActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SignupActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(SignupActivity.this, "Database error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.loginRedirectText.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });
    }

    private boolean validateInputs(String email, String password, String confirm, String name, String age, String phone) {
        // Kiểm tra các trường không được bỏ trống
        if (email.isEmpty() || password.isEmpty() || confirm.isEmpty() || name.isEmpty() || age.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Kiểm tra định dạng email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Kiểm tra mật khẩu và mật khẩu xác nhận phải giống nhau
        if (!password.equals(confirm)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Kiểm tra tuổi hợp lệ
        try {
            int parsedAge = Integer.parseInt(age);
            if (parsedAge <= 0) {
                Toast.makeText(this, "Age must be a positive number", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid age format", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Kiểm tra định dạng số điện thoại (nếu cần)
        if (!Patterns.PHONE.matcher(phone).matches()) {
            Toast.makeText(this, "Invalid phone number format", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}

