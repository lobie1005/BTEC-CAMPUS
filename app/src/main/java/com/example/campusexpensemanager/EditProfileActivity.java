package com.example.campusexpensemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editName;
    private EditText editAge;
    private EditText editPhone;
    private Button updateButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editName = findViewById(R.id.editName);
        editAge = findViewById(R.id.editAge);
        editPhone = findViewById(R.id.editPhone);
        updateButton = findViewById(R.id.updateButton);

        databaseHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        String email = intent.getStringExtra("EMAIL_KEY");
        if (email != null) {
            // Load existing data for the user
            User user = databaseHelper.getUserDetails(email);
            if (user != null) {
                editName.setText(user.getName());
                editAge.setText(String.valueOf(user.getAge()));
                editPhone.setText(user.getPhone());
            }
        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString();
                String ageStr = editAge.getText().toString();
                String phone = editPhone.getText().toString();

                if (name.isEmpty() || ageStr.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(EditProfileActivity.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidAge(ageStr)) {
                    Toast.makeText(EditProfileActivity.this, "Please enter a valid age (1-120)", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidPhoneNumber(phone)) {
                    Toast.makeText(EditProfileActivity.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                    return;
                }

                int age = Integer.parseInt(ageStr);
                String email = getIntent().getStringExtra("EMAIL_KEY");

                boolean update = databaseHelper.updateUserDetails(email, name, age, phone);
                if (update) {
                    Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    // Update SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("EMAIL_KEY", email);
                    editor.putString("HNAME_KEY", name);
                    editor.putString("NAME_KEY", name);
                    editor.putInt("AGE_KEY", age);
                    editor.putString("PHONE_KEY", phone);
                    editor.apply();
                    Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValidAge(String ageStr) {
        try {
            if (!ageStr.matches("\\d+")) {  // Kiểm tra xem chuỗi chỉ chứa số
                return false;
            }
            int age = Integer.parseInt(ageStr);
            return age > 0; // Giới hạn tuổi hợp lệ từ 1 đến 120
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidPhoneNumber(String phone) {
        // Kiểm tra xem chuỗi chỉ chứa số
        if (!phone.matches("\\d+")) {
            return false;
        }
        // Kiểm tra định dạng số điện thoại hợp lệ
        return Patterns.PHONE.matcher(phone).matches();
    }
}
