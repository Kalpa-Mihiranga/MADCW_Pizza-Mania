package com.example.pizzamania;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomerDashboard extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String> productNames, productDescriptions;
    ArrayList<byte[]> productImages;
    ArrayList<Double> smallPrices, mediumPrices, largePrices;
    SqliteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        recyclerView = findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columns

        dbHelper = new SqliteHelper(this);

        loadProductsFromDB();

        ProductAdapter adapter = new ProductAdapter(
                this,
                productNames,
                productDescriptions,
                productImages,
                smallPrices,
                mediumPrices,
                largePrices
        );

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(position -> {
            Intent intent = new Intent(CustomerDashboard.this, ProductDetailActivity.class);
            intent.putExtra("name", productNames.get(position));
            intent.putExtra("description", productDescriptions.get(position));
            intent.putExtra("smallPrice", smallPrices.get(position));
            intent.putExtra("mediumPrice", mediumPrices.get(position));
            intent.putExtra("largePrice", largePrices.get(position));
            intent.putExtra("image", productImages.get(position));
            startActivity(intent);
        });
    }

    private void loadProductsFromDB() {
        productNames = new ArrayList<>();
        productDescriptions = new ArrayList<>();
        productImages = new ArrayList<>();
        smallPrices = new ArrayList<>();
        mediumPrices = new ArrayList<>();
        largePrices = new ArrayList<>();

        Cursor cursor = dbHelper.getAllProducts();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                productNames.add(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                productDescriptions.add(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                productImages.add(cursor.getBlob(cursor.getColumnIndexOrThrow("image")));
                smallPrices.add(cursor.getDouble(cursor.getColumnIndexOrThrow("smallPrice")));
                mediumPrices.add(cursor.getDouble(cursor.getColumnIndexOrThrow("mediumPrice")));
                largePrices.add(cursor.getDouble(cursor.getColumnIndexOrThrow("largePrice")));
            } while (cursor.moveToNext());
            cursor.close();
        }
    }
}
