package com.example.campusexpensemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView emailTextView;
    private TextView hnameTextView;
    private TextView nameTextView;
    private TextView ageTextView;
    private TextView phoneTextView;
    private TextView logoutTextView;
    private Button changePasswordButton;
    private Button editProfileButton;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        emailTextView = findViewById(R.id.emailTextView);
        hnameTextView = findViewById(R.id.hnameTextView);
        nameTextView = findViewById(R.id.nameTextView);
        ageTextView = findViewById(R.id.ageTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        logoutTextView = findViewById(R.id.logoutTextView);
        changePasswordButton = findViewById(R.id.changePasswordTextView);
        editProfileButton = findViewById(R.id.editProfileTextView);

        // Lấy dữ liệu từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("EMAIL_KEY", "default@example.com");
        String hname = sharedPreferences.getString("HNAME_KEY", "Default Name");
        String name = sharedPreferences.getString("NAME_KEY", "Default Name");
        int age = sharedPreferences.getInt("AGE_KEY", 18);
        String phone = sharedPreferences.getString("PHONE_KEY", "0000000000");


        // Hiển thị dữ liệu lên các TextView
        emailTextView.setText(email);
        hnameTextView.setText(hname);
        nameTextView.setText(name);
        ageTextView.setText(String.valueOf(age));
        phoneTextView.setText(phone);
        // Xử lý sự kiện đăng xuất
        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xóa thông tin đăng nhập
                SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear(); // Xóa tất cả dữ liệu
                editor.apply();

                // Chuyển hướng đến LoginActivity và xóa tất cả Activity trước đó
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginIntent);
                finish();

            }
        });

        // Xử lý sự kiện đổi mật khẩu
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailTextView.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Email không khả dụng", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(MainActivity.this, ChangePasswordActivity.class);
                intent.putExtra("EMAIL_KEY", email);
                startActivity(intent);
            }
        });

        // Xử lý sự kiện chỉnh sửa thông tin cá nhân
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailTextView.getText().toString();
                Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
                intent.putExtra("EMAIL_KEY", email);
                startActivity(intent);
            }
        });

        // Xử lý nút quay lại
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
    }
}