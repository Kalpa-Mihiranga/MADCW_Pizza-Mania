package com.example.pizzamania;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CheckoutActivity extends AppCompatActivity {

    EditText editCustomerName, editCustomerPhone, editCustomerAddress;
    TextView txtTotalPrice;
    ListView listViewCheckout;
    Button btnPlaceOrder;

    CartAdapter adapter;
    SqliteHelper dbHelper;
    int customerId = 1; // 👉 Replace with logged-in customer id from login session
    String sessionEmail; // store logged-in email

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        editCustomerName = findViewById(R.id.editCustomerName);
        editCustomerPhone = findViewById(R.id.editCustomerPhone);
        editCustomerAddress = findViewById(R.id.editCustomerAddress);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        listViewCheckout = findViewById(R.id.listViewCheckout);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);

        dbHelper = new SqliteHelper(this);

        // Get logged-in email from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        sessionEmail = sharedPreferences.getString("email", null);

        // Load customer info into EditTexts using session email
        loadCustomerInfo(sessionEmail);

        // Show cart items
        adapter = new CartAdapter(this, CartManager.getInstance().getCartItems());
        listViewCheckout.setAdapter(adapter);

        // Show total price
        txtTotalPrice.setText("Total: Rs. " + CartManager.getInstance().getTotalPrice());

        // Place Order Button
        btnPlaceOrder.setOnClickListener(v -> {
            String name = editCustomerName.getText().toString().trim();
            String phone = editCustomerPhone.getText().toString().trim();
            String address = editCustomerAddress.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ Update customer info before placing order
            int rows = dbHelper.updateCustomer(customerId, name, "test@gmail.com", address, phone);
            if (rows > 0) {
                Toast.makeText(this, "Customer info updated", Toast.LENGTH_SHORT).show();
            }

            if (CartManager.getInstance().getCartItems().isEmpty()) {
                Toast.makeText(this, "Cart is empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            // 👉 Save order to database (if you create orders table)
            Toast.makeText(this, "✅ Order placed successfully!", Toast.LENGTH_LONG).show();

            // Clear cart
            CartManager.getInstance().clearCart();
            adapter.notifyDataSetChanged();

            // Go to payment gateway
            Intent intent = new Intent(CheckoutActivity.this, PaymentGatewayActivity.class);
            intent.putExtra("customerName", name);
            intent.putExtra("customerPhone", phone);
            intent.putExtra("customerAddress", address);
            intent.putExtra("totalPrice", CartManager.getInstance().getTotalPrice());
            startActivity(intent);
            finish();
        });
    }

    private void loadCustomerInfo(String email) {
        Cursor cursor = dbHelper.getCustomerByEmail(email);
        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("customer_name"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));

            editCustomerName.setText(name);
            editCustomerPhone.setText(phone);
            editCustomerAddress.setText(address);

            // Save customerId for update
            customerId = cursor.getInt(cursor.getColumnIndexOrThrow("customer_ID"));

            cursor.close();
        }
    }
}
