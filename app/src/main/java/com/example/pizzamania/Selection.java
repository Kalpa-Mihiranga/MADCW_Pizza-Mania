package com.example.pizzamania;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Selection extends AppCompatActivity {

    private ImageButton adminButton, customerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection); // Make sure your XML file is named activity_selection.xml

        // Initialize buttons
        adminButton = findViewById(R.id.adminButton);
        customerButton = findViewById(R.id.customerButton);

        // Admin button click
        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Admin Login Activity
                Intent intent = new Intent(Selection.this, AdminLogin.class);
                startActivity(intent);
            }
        });

        // Customer button click
        customerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Customer Login Activity
                Intent intent = new Intent(Selection.this, UserLogin.class);
                startActivity(intent);
            }
        });
    }
}