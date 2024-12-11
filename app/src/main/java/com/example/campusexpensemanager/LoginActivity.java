package com.example.campusexpensemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusexpensemanager.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.loginEmail.getText().toString().trim();
                String password = binding.loginPassword.getText().toString().trim();

                if (email.equals("") || password.equals("")) {
                    Toast.makeText(LoginActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                } else {
                    boolean checkCredentials = databaseHelper.checkEmailPassword(email, password);
                    DataStatic.email = email;
                    DataStatic.password = password;
                    if (checkCredentials) {
                        User user = databaseHelper.getUserDetails(email);
                        if (user != null) {
                            Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MenuActivity.class);

                            // Lưu thông tin người dùng vào SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("EMAIL_KEY", user.getEmail());
                            editor.putString("HNAME_KEY", user.getName());
                            editor.putString("NAME_KEY", user.getName());
                            editor.putInt("AGE_KEY", user.getAge());
                            editor.putString("PHONE_KEY", user.getPhone());
                            editor.apply();
                            editor.putString("email", email);
                            editor.putString("password", password);  // Store hashed/encrypted version instead
                            editor.apply();  // or use commit() for synchronous saving




                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "User details not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}
