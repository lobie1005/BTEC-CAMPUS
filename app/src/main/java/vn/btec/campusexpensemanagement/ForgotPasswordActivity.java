package vn.btec.campusexpensemanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText emailInput;
    private Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailInput = findViewById(R.id.email_input);
        resetButton = findViewById(R.id.reset_button);

        resetButton.setOnClickListener(v -> {
            // This is just a placeholder for now
            Toast.makeText(this, "Password reset functionality will be implemented later", Toast.LENGTH_SHORT).show();
        });
    }
}
