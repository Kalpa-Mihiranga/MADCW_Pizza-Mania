package com.example.pizzamania;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CartActivity extends AppCompatActivity {

    ListView listViewCart;
    TextView txtTotalPrice;
    Button btnCheckout;

    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        listViewCart = findViewById(R.id.listViewCart);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);

        adapter = new CartAdapter(this, CartManager.getInstance().getCartItems());
        listViewCart.setAdapter(adapter);

        updateTotal();

        // Listen for changes to update total price
        adapter.registerDataSetObserver(new android.database.DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                updateTotal();
            }
        });

        btnCheckout.setOnClickListener(v -> {
            Intent checkoutIntent = new Intent(CartActivity.this, CheckoutActivity.class);
            startActivity(checkoutIntent);
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_cart);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                // Navigate to Dashboard
                startActivity(new Intent(CartActivity.this, CustomerDashboard.class));
                finish();
                return true;

            } else if (id == R.id.nav_cart) {
                // Stay on Cart
                return true;

            } else if (id == R.id.nav_orders) {
                // Navigate to Orders page
                startActivity(new Intent(CartActivity.this, Orders.class));
                finish();
                return true;

            } else if (id == R.id.nav_profile) {
                // Navigate to Profile page
                startActivity(new Intent(CartActivity.this, ProfileActivity.class));
                finish();
                return true;
            }

            return false;
        });
    }

    private void updateTotal() {
        txtTotalPrice.setText("Total: Rs. " + CartManager.getInstance().getTotalPrice());
    }
}
