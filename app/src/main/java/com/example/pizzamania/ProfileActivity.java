package com.example.pizzamania;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    TextView txtName, txtEmail, txtAddress, txtPhone;
    Button btnEditProfile, btnLogout;
    SqliteHelper dbHelper;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtAddress = findViewById(R.id.txtAddress);
        txtPhone = findViewById(R.id.txtPhone);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnLogout = findViewById(R.id.btnLogout);

        dbHelper = new SqliteHelper(this);

        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        userEmail = sharedPreferences.getString("email", "");

        loadUserDetails();

        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ProfileEditActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> showLogoutDialog(sharedPreferences));
    }

    private void loadUserDetails() {
        Cursor cursor = dbHelper.getCustomerByEmail(userEmail);
        if(cursor != null && cursor.moveToFirst()) {
            txtName.setText(cursor.getString(cursor.getColumnIndexOrThrow("customer_name")));
            txtEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            txtAddress.setText(cursor.getString(cursor.getColumnIndexOrThrow("address")));
            txtPhone.setText(cursor.getString(cursor.getColumnIndexOrThrow("phone")));
            cursor.close();
        }
    }

    private void showLogoutDialog(SharedPreferences sharedPreferences) {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                    // Navigate to Selection page
                    Intent intent = new Intent(ProfileActivity.this, Selection.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserDetails(); // refresh details after editing
    }
}
