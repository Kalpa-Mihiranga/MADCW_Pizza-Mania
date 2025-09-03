package com.example.pizzamania;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserLogin extends AppCompatActivity {

    EditText emailInput, passwordInput;
    Button loginBtn;
    TextView txtRegister;
    SqliteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Bind views
        emailInput = findViewById(R.id.editTextTextEmailAddress);
        passwordInput = findViewById(R.id.editTextTextPassword);
        loginBtn = findViewById(R.id.button);
        txtRegister = findViewById(R.id.textRegister);

        // Initialize DB helper
        dbHelper = new SqliteHelper(this);

        // Login button click
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                if(email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(UserLogin.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check user in SQLite
                boolean userExists = dbHelper.checkUser(email, password);

                if(userExists) {
                    // Successful login → navigate to CustomerDashboard
                    Toast.makeText(UserLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserLogin.this, CustomerDashboard.class);
                    startActivity(intent);

                    // Close login activity so back button doesn't return here
                    finish();
                } else {
                    // Invalid credentials
                    Toast.makeText(UserLogin.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Register text click
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserLogin.this, UserRegister.class);
                startActivity(intent);
            }
        });
    }
}
