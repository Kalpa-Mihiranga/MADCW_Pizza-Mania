package com.example.pizzamania;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    ListView listViewCart;
    TextView txtTotalPrice;
    Button btnCheckout;

    ArrayList<String> cartItems;
    double totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        listViewCart = findViewById(R.id.listViewCart);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);

        cartItems = new ArrayList<>();

        // Get data from ProductDetailActivity
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String priceStr = intent.getStringExtra("price");

        if (name != null && priceStr != null) {
            cartItems.add(name + " - Rs. " + priceStr);
            totalPrice += Double.parseDouble(priceStr);
        }

        // Display items
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cartItems);
        listViewCart.setAdapter(adapter);

        txtTotalPrice.setText("Total: Rs. " + totalPrice);

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to CheckoutActivity
                Intent checkoutIntent = new Intent(CartActivity.this, CheckoutActivity.class);
                checkoutIntent.putStringArrayListExtra("cartItems", cartItems);
                checkoutIntent.putExtra("totalPrice", totalPrice);
                startActivity(checkoutIntent);
            }
        });
    }
}
