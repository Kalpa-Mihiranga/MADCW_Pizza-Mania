package com.example.pizzamania;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboard extends AppCompatActivity {

    Button btnAddProduct, btnViewCustomers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnViewCustomers = findViewById(R.id.btnViewCustomers);

        // Navigate to Add Product
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboard.this, ProductAddActivity.class));
            }
        });

        // Navigate to Customer List (optional, if you want)
        btnViewCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add your CustomerListActivity later
            }
        });
    }
}
