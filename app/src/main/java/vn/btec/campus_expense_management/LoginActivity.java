package vn.btec.campus_expense_management;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import vn.btec.campus_expense_management.database.DatabaseHelper;
import vn.btec.campus_expense_management.utils.hashPassword;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailInput, passwordInput;
    private TextInputLayout emailLayout, passwordLayout;
    private MaterialButton loginButton, registerButton;
    private DatabaseHelper databaseHelper;
    private TextView registerTextClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login); // Layout đăng nhập

        // Khởi tạo các thành phần giao diện
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        emailLayout = findViewById(R.id.tilEmail);
        passwordLayout = findViewById(R.id.tilPassword);
        loginButton = findViewById(R.id.login_button);
        registerTextClick = findViewById(R.id.goto_register);

        databaseHelper = new DatabaseHelper(this);

        // Xử lý sự kiện đăng nhập
        loginButton.setOnClickListener(v -> handleLogin());

        // Chuyển sang màn hình đăng ký
        registerTextClick.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void handleLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Kiểm tra tính hợp lệ của email và mật khẩu
        if (TextUtils.isEmpty(email)) {
            emailLayout.setError("Email cannot be empty");
            return;
        } else {
            emailLayout.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            passwordLayout.setError("Password cannot be empty");
            return;
        } else {
            passwordLayout.setError(null);
        }

        // Mã hóa mật khẩu
        String hashedPassword = hashPassword.hashPassword(password);

        // Thực hiện đăng nhập
        int userId = databaseHelper.getUserIdByEmailAndPassword(email, hashedPassword);
        if (userId > 0) { // Check if user_id is valid
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
            // Chuyển sang MainActivity nếu đăng nhập thành công
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("user_id", userId); // Pass user_id to MainActivity
            //set user_id to DataStatic
            DataStatic.setUserId(userId);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }
}
