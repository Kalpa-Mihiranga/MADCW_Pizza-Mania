package com.example.pizzamania;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminLogin extends AppCompatActivity {

    private EditText editEmail, editPassword;
    private Spinner spinnerBranch;
    private Button btnLogin;

    // demo credentials (change to secure storage later)
    private final String ADMIN_EMAIL = "admin@pizza.com";
    private final String ADMIN_PASSWORD = "12345";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        // Initialize views
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        spinnerBranch = findViewById(R.id.spinnerBranch); // spinner from XML
        btnLogin = findViewById(R.id.btnLogin);

        // Set up branch options in spinner
        String[] branches = {"Colombo", "Galle"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, branches);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBranch.setAdapter(adapter);

        // Handle login button click
        btnLogin.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();
            String selectedBranch = spinnerBranch.getSelectedItem().toString();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Enter email & password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (email.equals(ADMIN_EMAIL) && password.equals(ADMIN_PASSWORD)) {
                // pass selected branch to next activity
                Intent intent = new Intent(AdminLogin.this, ProductAddActivity.class);
                intent.putExtra("branch", selectedBranch); // send branch info
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

