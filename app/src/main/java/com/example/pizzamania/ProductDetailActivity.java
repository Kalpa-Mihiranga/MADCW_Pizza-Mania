package com.example.pizzamania;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailActivity extends AppCompatActivity {

    TextView txtProductName, txtProductPrice, txtProductDescription;
    Button btnAddToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        txtProductName = findViewById(R.id.txtProductName);
        txtProductPrice = findViewById(R.id.txtProductPrice);
        txtProductDescription = findViewById(R.id.txtProductDescription);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        // Get data from previous activity (e.g., Product list)
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String price = intent.getStringExtra("price");
        String description = intent.getStringExtra("description");

        txtProductName.setText(name);
        txtProductPrice.setText("Rs. " + price);
        txtProductDescription.setText(description);

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to CartActivity (implement later)
                Intent cartIntent = new Intent(ProductDetailActivity.this, CartActivity.class);
                cartIntent.putExtra("name", name);
                cartIntent.putExtra("price", price);
                startActivity(cartIntent);
            }
        });
    }
}
