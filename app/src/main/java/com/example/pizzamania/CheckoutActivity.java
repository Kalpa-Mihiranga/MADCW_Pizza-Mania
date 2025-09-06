package com.example.pizzamania;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CheckoutActivity extends AppCompatActivity {

    TextView txtCustomerName, txtCustomerPhone, txtCustomerAddress, txtTotalPrice;
    ListView listViewCheckout;
    Button btnPlaceOrder, btnCancel;

    CartAdapter adapter;
    SqliteHelper dbHelper;
    String sessionEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Bind Views
        txtCustomerName = findViewById(R.id.txtCustomerName);
        txtCustomerPhone = findViewById(R.id.txtCustomerPhone);
        txtCustomerAddress = findViewById(R.id.txtCustomerAddress);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        listViewCheckout = findViewById(R.id.listViewCheckout);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        btnCancel = findViewById(R.id.btnCancel);

        dbHelper = new SqliteHelper(this);

        // Get logged-in email from session
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        sessionEmail = sharedPreferences.getString("email", null);

        // Load customer info (read-only)
        loadCustomerInfo(sessionEmail);

        // Show cart items (read-only, no quantity change)
        adapter = new CartAdapter(this, CartManager.getInstance().getCartItems()) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                // Hide increase/decrease/remove buttons in checkout
                View btnIncrease = view.findViewById(R.id.btnIncrease);
                View btnDecrease = view.findViewById(R.id.btnDecrease);
                View btnRemove = view.findViewById(R.id.btnRemove);
                if (btnIncrease != null) btnIncrease.setVisibility(View.GONE);
                if (btnDecrease != null) btnDecrease.setVisibility(View.GONE);
                if (btnRemove != null) btnRemove.setVisibility(View.GONE);
                return view;
            }
        };
        listViewCheckout.setAdapter(adapter);

        // Show total price
        txtTotalPrice.setText("Total: Rs. " + CartManager.getInstance().getTotalPrice());

        // Place Order
        btnPlaceOrder.setOnClickListener(v -> {
            if (CartManager.getInstance().getCartItems().isEmpty()) {
                Toast.makeText(this, "Cart is empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "✅ Order placed successfully!", Toast.LENGTH_LONG).show();

            // Clear cart
            CartManager.getInstance().clearCart();
            adapter.notifyDataSetChanged();

            // Go to payment gateway
            Intent intent = new Intent(CheckoutActivity.this, PaymentGatewayActivity.class);
            intent.putExtra("customerName", txtCustomerName.getText().toString());
            intent.putExtra("customerPhone", txtCustomerPhone.getText().toString());
            intent.putExtra("customerAddress", txtCustomerAddress.getText().toString());
            intent.putExtra("totalPrice", CartManager.getInstance().getTotalPrice());
            startActivity(intent);
            finish();
        });

        // Cancel button
        btnCancel.setOnClickListener(v -> {
            finish(); // just close checkout
        });
    }

    private void loadCustomerInfo(String email) {
        Cursor cursor = dbHelper.getCustomerByEmail(email);
        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("customer_name"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));

            txtCustomerName.setText("Name: " + name);
            txtCustomerPhone.setText("Phone: " + phone);
            txtCustomerAddress.setText("Address: " + address);

            cursor.close();
        }
    }
}
