package com.example.pizzamania;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CustomerDashboard extends AppCompatActivity {

    ListView listViewProducts;
    ArrayList<String> productNames;
    ArrayList<String> productDescriptions;
    ArrayList<byte[]> productImages;
    ArrayList<Double> smallPrices;
    ArrayList<Double> mediumPrices;
    ArrayList<Double> largePrices;

    SqliteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        listViewProducts = findViewById(R.id.listViewProducts);
        dbHelper = new SqliteHelper(this);

        loadProductsFromDB();

        ProductAdapter adapter = new ProductAdapter(
                this,
                productNames,
                productDescriptions,
                productImages
        );
        listViewProducts.setAdapter(adapter);

        listViewProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CustomerDashboard.this, ProductDetailActivity.class);
                intent.putExtra("name", productNames.get(position));
                intent.putExtra("description", productDescriptions.get(position));
                intent.putExtra("smallPrice", smallPrices.get(position));
                intent.putExtra("mediumPrice", mediumPrices.get(position));
                intent.putExtra("largePrice", largePrices.get(position));
                intent.putExtra("image", productImages.get(position));
                startActivity(intent);
            }
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
