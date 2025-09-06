package com.example.pizzamania;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentGatewayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);

        String name = getIntent().getStringExtra("customerName");
        String phone = getIntent().getStringExtra("customerPhone");
        String address = getIntent().getStringExtra("customerAddress");
        double totalPrice = getIntent().getDoubleExtra("totalPrice", 0);

        TextView txtName = findViewById(R.id.txtName);
        TextView txtPhone = findViewById(R.id.txtPhone);
        TextView txtAddress = findViewById(R.id.txtAddress);
        TextView txtTotal = findViewById(R.id.txtTotal);
        Button btnPay = findViewById(R.id.btnPay);

        txtName.setText("Name: " + name);
        txtPhone.setText("Phone: " + phone);
        txtAddress.setText("Address: " + address);
        txtTotal.setText("Total: Rs. " + totalPrice);

        btnPay.setOnClickListener(v -> {
            Toast.makeText(this, "Payment Successful!", Toast.LENGTH_LONG).show();
            finish();
        });
    }
}

