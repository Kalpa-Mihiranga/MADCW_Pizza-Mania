package com.example.pizzamania;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        listViewProducts = findViewById(R.id.listViewProducts);

        // Sample products
        productNames = new ArrayList<>();
        productPrices = new ArrayList<>();
        productDescriptions = new ArrayList<>();

        productNames.add("Margherita Pizza");
        productPrices.add("500");
        productDescriptions.add("Classic cheese and tomato pizza.");

        productNames.add("Pepperoni Pizza");
        productPrices.add("700");
        productDescriptions.add("Spicy pepperoni with mozzarella cheese.");

        productNames.add("Veggie Pizza");
        productPrices.add("600");
        productDescriptions.add("Fresh vegetables with tomato sauce and cheese.");

        // Adapter to show product names in list
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productNames);
        listViewProducts.setAdapter(adapter);

        // On item click, go to ProductDetailActivity
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
}
