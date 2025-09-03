package com.example.pizzamania;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CustomerDashboard extends AppCompatActivity {

    ListView listViewProducts;
    ArrayList<String> productNames;
    ArrayList<String> productPrices;
    ArrayList<String> productDescriptions;
    SqliteHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        listViewProducts = findViewById(R.id.listViewProducts);
        dbHelper = new SqliteHelper(this);

        // Load products from DB
        loadProductsFromDB();

        // Adapter to show product names in list
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                productNames
        );
        listViewProducts.setAdapter(adapter);

        // On item click → ProductDetailActivity
        listViewProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CustomerDashboard.this, ProductDetailActivity.class);
                intent.putExtra("name", productNames.get(position));
                intent.putExtra("price", productPrices.get(position));
                intent.putExtra("description", productDescriptions.get(position));
                startActivity(intent);
            }
        });
    }

    private void loadProductsFromDB() {
        productNames = new ArrayList<>();
        productPrices = new ArrayList<>();
        productDescriptions = new ArrayList<>();

        Cursor cursor = dbHelper.getAllProducts();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow("description"));

                productNames.add(name);
                productPrices.add(price);
                productDescriptions.add(desc);
            } while (cursor.moveToNext());
            cursor.close();
        }
    }
}
