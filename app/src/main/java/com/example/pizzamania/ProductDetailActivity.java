package com.example.pizzamania;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailActivity extends AppCompatActivity {

    TextView txtProductName, txtProductPrice, txtProductDescription;
    ImageView imgProduct;
    Button btnAddToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        txtProductName = findViewById(R.id.txtProductName);
        txtProductPrice = findViewById(R.id.txtProductPrice);
        txtProductDescription = findViewById(R.id.txtProductDescription);
        imgProduct = findViewById(R.id.imgProduct);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        // Get data from previous activity
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String price = intent.getStringExtra("price");
        String description = intent.getStringExtra("description");
        byte[] imageBytes = intent.getByteArrayExtra("image"); // get image bytes

        txtProductName.setText(name);
        txtProductPrice.setText("Rs. " + price);
        txtProductDescription.setText(description);

        if (imageBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            imgProduct.setImageBitmap(bitmap);
        }

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to CartActivity (implement later)
                Intent cartIntent = new Intent(ProductDetailActivity.this, CartActivity.class);
                cartIntent.putExtra("name", name);
                cartIntent.putExtra("price", price);
                cartIntent.putExtra("image", imageBytes); // pass image to cart if needed
                startActivity(cartIntent);
            }
        });
    }
}