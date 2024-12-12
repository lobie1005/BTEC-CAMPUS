package vn.btec.campus_expense_management;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import vn.btec.campus_expense_management.database.DatabaseHelper;
import vn.btec.campus_expense_management.utils.hashPassword;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText firstNameInput, lastNameInput, emailInput, passwordInput, confirmPasswordInput;
    private MaterialButton registerButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register); // Layout đăng ký

        // Khởi tạo các thành phần giao diện
        firstNameInput = findViewById(R.id.firstName);
        lastNameInput = findViewById(R.id.lastName);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        confirmPasswordInput = findViewById(R.id.confirmPassword);
        registerButton = findViewById(R.id.register_button);

        databaseHelper = new DatabaseHelper(this);

        // Xử lý sự kiện đăng ký
        registerButton.setOnClickListener(v -> handleRegister());
    }

    private void handleRegister() {
        String firstName = firstNameInput.getText().toString().trim();
        String lastName = lastNameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();
        // Kiểm tra tính hợp lệ của dữ liệu đầu vào
        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)) {
            Toast.makeText(this, "Name fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Password fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        // Mã hóa mật khẩu
        String hashedPassword = hashPassword.hashPassword(password);
        // Thêm người dùng vào cơ sở dữ liệu
        boolean isRegistered = databaseHelper.signUp(email, hashedPassword, firstName, lastName);
        if (isRegistered) {
            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
            finish(); // Quay lại màn hình đăng nhập
        } else {
            Toast.makeText(this, "Registration failed. Email may already exist.", Toast.LENGTH_SHORT).show();
        }
    }
}
