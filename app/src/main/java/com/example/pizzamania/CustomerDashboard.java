package com.example.pizzamania;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CustomerDashboard extends AppCompatActivity {

    ListView listViewProducts;
    ArrayList<String> productNames;
    ArrayList<String> productPrices;
    ArrayList<String> productDescriptions;
    ArrayList<byte[]> productImages;
    SqliteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        listViewProducts = findViewById(R.id.listViewProducts);
        dbHelper = new SqliteHelper(this);

        // Load products
        loadProductsFromDB();

        // Custom adapter
        ProductAdapter adapter = new ProductAdapter(
                this,
                productNames,
                productPrices,
                productImages
        );
        listViewProducts.setAdapter(adapter);

        // Item click → ProductDetailActivity
        listViewProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CustomerDashboard.this, ProductDetailActivity.class);
                intent.putExtra("name", productNames.get(position));
                intent.putExtra("price", productPrices.get(position));
                intent.putExtra("description", productDescriptions.get(position));
                intent.putExtra("image", productImages.get(position));
                startActivity(intent);
            }
        });
    }

    private void loadProductsFromDB() {
        productNames = new ArrayList<>();
        productPrices = new ArrayList<>();
        productDescriptions = new ArrayList<>();
        productImages = new ArrayList<>();

        Cursor cursor = dbHelper.getAllProducts();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                productNames.add(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                productPrices.add(cursor.getString(cursor.getColumnIndexOrThrow("price")));
                productDescriptions.add(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                productImages.add(cursor.getBlob(cursor.getColumnIndexOrThrow("image")));
            } while (cursor.moveToNext());
            cursor.close();
        }
    }
}