package com.example.pizzamania;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailActivity extends AppCompatActivity {

    TextView txtProductName, txtProductPrice, txtProductDescription;
    ImageView imgProduct;
    Button btnAddToCart;
    CheckBox cbCheese, cbOlives, cbSausage;
    RadioGroup rgSize;
    RadioButton rbSmall, rbMedium, rbLarge;

    double smallPrice, mediumPrice, largePrice;
    double basePrice, finalPrice;
    byte[] imageBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Initialize views
        txtProductName = findViewById(R.id.txtProductName);
        txtProductPrice = findViewById(R.id.txtProductPrice);
        txtProductDescription = findViewById(R.id.txtProductDescription);
        imgProduct = findViewById(R.id.imgProduct);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        cbCheese = findViewById(R.id.cbCheese);
        cbOlives = findViewById(R.id.cbOlives);
        cbSausage = findViewById(R.id.cbSausage);
        rgSize = findViewById(R.id.rgSize);
        rbSmall = findViewById(R.id.rbSmall);
        rbMedium = findViewById(R.id.rbMedium);
        rbLarge = findViewById(R.id.rbLarge);

        // Get product details from intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");
        smallPrice = intent.getDoubleExtra("smallPrice", 0);
        mediumPrice = intent.getDoubleExtra("mediumPrice", 0);
        largePrice = intent.getDoubleExtra("largePrice", 0);
        imageBytes = intent.getByteArrayExtra("image");

        txtProductName.setText(name);
        txtProductDescription.setText(description);

        // Set product image
        if (imageBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            imgProduct.setImageBitmap(bitmap);
        }

        // Show prices on RadioButtons dynamically
        rbSmall.setText("Small (Rs. " + smallPrice + ")");
        rbMedium.setText("Medium (Rs. " + mediumPrice + ")");
        rbLarge.setText("Large (Rs. " + largePrice + ")");

        // Default selection
        rbSmall.setChecked(true);
        basePrice = smallPrice;
        finalPrice = basePrice;

        // Size change listener
        rgSize.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbSmall) basePrice = smallPrice;
            else if (checkedId == R.id.rbMedium) basePrice = mediumPrice;
            else if (checkedId == R.id.rbLarge) basePrice = largePrice;
            updatePrice();
        });

        // Toppings listeners
        cbCheese.setOnCheckedChangeListener((buttonView, isChecked) -> updatePrice());
        cbOlives.setOnCheckedChangeListener((buttonView, isChecked) -> updatePrice());
        cbSausage.setOnCheckedChangeListener((buttonView, isChecked) -> updatePrice());

        // Add to cart button
        btnAddToCart.setOnClickListener(v -> {
            // TODO: Add to cart logic here
            txtProductPrice.setText("Added to cart: Rs. " + finalPrice);
        });

        // Initial price display
        updatePrice();
    }

    private void updatePrice() {
        finalPrice = basePrice;

        // Add toppings
        if (cbCheese.isChecked()) finalPrice += 100;
        if (cbOlives.isChecked()) finalPrice += 80;
        if (cbSausage.isChecked()) finalPrice += 150;

        txtProductPrice.setText("Rs. " + finalPrice);
    }
}
