package com.btec.fpt.campus_expense_manager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.btec.fpt.campus_expense_manager.database.DatabaseHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;


public class AuthActivity extends AppCompatActivity {

    private TextInputLayout emailLayout, passwordLayout;
    private MaterialButton loginButton, registerButton;
    private DataStatic dataStatic;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
    }


    //get email and password from user input with id
    EditText emailInput = findViewById(R.id.email);
    EditText passwordInput = findViewById(R.id.password);

}
