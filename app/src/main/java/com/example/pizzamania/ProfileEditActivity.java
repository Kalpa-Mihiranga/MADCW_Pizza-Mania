package com.example.pizzamania;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileEditActivity extends AppCompatActivity {

    EditText etName, etEmail, etAddress, etPhone;
    Button btnSaveProfile;
    SqliteHelper dbHelper;
    String userEmail;
    int customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        etName = findViewById(R.id.editName);
        etEmail = findViewById(R.id.editEmail);
        etAddress = findViewById(R.id.editAddress);
        etPhone = findViewById(R.id.editPhone);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);

        dbHelper = new SqliteHelper(this);

        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        userEmail = sharedPreferences.getString("email", "");

        loadUserDetails();

        btnSaveProfile.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();

            if(name.isEmpty() || email.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int result = dbHelper.updateCustomer(customerId, name, email, address, phone);
            if(result > 0){
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserDetails() {
        Cursor cursor = dbHelper.getCustomerByEmail(userEmail);
        if(cursor != null && cursor.moveToFirst()) {
            customerId = cursor.getInt(cursor.getColumnIndexOrThrow("customer_ID"));
            etName.setText(cursor.getString(cursor.getColumnIndexOrThrow("customer_name")));
            etEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            etAddress.setText(cursor.getString(cursor.getColumnIndexOrThrow("address")));
            etPhone.setText(cursor.getString(cursor.getColumnIndexOrThrow("phone")));
            cursor.close();
        }
    }
}
