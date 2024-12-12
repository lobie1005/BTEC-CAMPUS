package vn.btec.campus_expense_management;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import vn.btec.campus_expense_management.database.DatabaseHelper;
import vn.btec.campus_expense_management.entities.BalanceInfor;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseHelper = new DatabaseHelper(this);

        // Retrieve user ID from intent
        int userId = getIntent().getIntExtra("user_id", -1);

        // Display user-specific data on the dashboard
        if (userId != -1) {
            displayDashboard(userId);
        }
    }

    private void displayDashboard(int userId) {
        BalanceInfor balanceInfor = databaseHelper.getBalanceFromUserId(userId);

        TextView balanceTextView = findViewById(R.id.balance_text);
        TextView incomeTextView = findViewById(R.id.income_text);
        TextView expenseTextView = findViewById(R.id.expense_text);

        if (balanceInfor != null) {
            balanceTextView.setText("Balance: " + balanceInfor.getBalance());
            incomeTextView.setText("Income: " + balanceInfor.getIncome());
            expenseTextView.setText("Expense: " + balanceInfor.getExpense());
        }
    }
}