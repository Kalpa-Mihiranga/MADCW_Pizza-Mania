package com.example.pizzamania;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

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

    // For search filtering
    ArrayList<String> allProductNames, allProductDescriptions;
    ArrayList<byte[]> allProductImages;
    ArrayList<Double> allSmallPrices, allMediumPrices, allLargePrices;
    ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        recyclerView = findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columns

        dbHelper = new SqliteHelper(this);

        // Load all products for filtering
        loadProductsFromDB();

        // Copy all data for search reference
        allProductNames = new ArrayList<>(productNames);
        allProductDescriptions = new ArrayList<>(productDescriptions);
        allProductImages = new ArrayList<>(productImages);
        allSmallPrices = new ArrayList<>(smallPrices);
        allMediumPrices = new ArrayList<>(mediumPrices);
        allLargePrices = new ArrayList<>(largePrices);

        adapter = new ProductAdapter(
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

        // Search bar logic
        EditText searchBar = findViewById(R.id.searchBar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
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

    private void filterProducts(String query) {
        productNames.clear();
        productDescriptions.clear();
        productImages.clear();
        smallPrices.clear();
        mediumPrices.clear();
        largePrices.clear();

        if (query.isEmpty()) {
            productNames.addAll(allProductNames);
            productDescriptions.addAll(allProductDescriptions);
            productImages.addAll(allProductImages);
            smallPrices.addAll(allSmallPrices);
            mediumPrices.addAll(allMediumPrices);
            largePrices.addAll(allLargePrices);
        } else {
            String lowerQuery = query.toLowerCase();
            for (int i = 0; i < allProductNames.size(); i++) {
                if (allProductNames.get(i).toLowerCase().contains(lowerQuery) ||
                    allProductDescriptions.get(i).toLowerCase().contains(lowerQuery)) {
                    productNames.add(allProductNames.get(i));
                    productDescriptions.add(allProductDescriptions.get(i));
                    productImages.add(allProductImages.get(i));
                    smallPrices.add(allSmallPrices.get(i));
                    mediumPrices.add(allMediumPrices.get(i));
                    largePrices.add(allLargePrices.get(i));
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}
