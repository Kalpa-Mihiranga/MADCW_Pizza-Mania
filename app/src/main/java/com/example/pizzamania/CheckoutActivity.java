package com.example.pizzamania;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {

    ListView listViewCheckout;
    TextView txtTotal;
    Button btnConfirmOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        listViewCheckout = findViewById(R.id.listViewCheckout);
        txtTotal = findViewById(R.id.txtTotal);
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder);

        // Get data from CartActivity
        ArrayList<String> cartItems = getIntent().getStringArrayListExtra("cartItems");
        double totalPrice = getIntent().getDoubleExtra("totalPrice", 0);

        if (cartItems != null && !cartItems.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cartItems);
            listViewCheckout.setAdapter(adapter);
        }

        txtTotal.setText("Total Amount: Rs. " + totalPrice);

        btnConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CheckoutActivity.this, "Order Confirmed!", Toast.LENGTH_SHORT).show();
                finish(); // Close activity after confirmation
            }
        });
    }
}
