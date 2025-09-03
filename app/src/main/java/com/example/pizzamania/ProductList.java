package com.example.pizzamania;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ProductList extends AppCompatActivity {

    ListView listView;
    SqliteHelper db;
    ArrayList<String> productNames;
    ArrayList<Integer> productIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        listView = findViewById(R.id.listViewProducts);
        db = new SqliteHelper(this);

        loadProducts();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            int productId = productIds.get(position);
            showEditDeleteDialog(productId);
        });
    }

    private void loadProducts() {
        productNames = new ArrayList<>();
        productIds = new ArrayList<>();

        var cursor = db.getAllProducts();
        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("product_ID"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                productIds.add(id);
                productNames.add(name);
            } while(cursor.moveToNext());
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productNames);
        listView.setAdapter(adapter);
    }

    private void showEditDeleteDialog(int productId) {
        var cursor = db.getAllProducts();
        if(cursor.moveToFirst()) {
            do {
                if(cursor.getInt(cursor.getColumnIndexOrThrow("product_ID")) == productId) {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    String desc = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                    double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                    byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));

                    showEditDialog(productId, name, desc, price, image);
                    break;
                }
            } while(cursor.moveToNext());
        }
        cursor.close();
    }

    private void showEditDialog(int id, String name, String desc, double price, byte[] image) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit/Delete Product");

        EditText etName = new EditText(this);
        etName.setText(name);
        EditText etDesc = new EditText(this);
        etDesc.setText(desc);
        EditText etPrice = new EditText(this);
        etPrice.setText(String.valueOf(price));

        Bitmap bmp = BitmapFactory.decodeByteArray(image,0,image.length);
        ImageView iv = new ImageView(this);
        iv.setImageBitmap(bmp);

        // Layout
        var layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.addView(etName);
        layout.addView(etDesc);
        layout.addView(etPrice);
        layout.addView(iv);

        builder.setView(layout);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String newName = etName.getText().toString().trim();
            String newDesc = etDesc.getText().toString().trim();
            double newPrice;
            try { newPrice = Double.parseDouble(etPrice.getText().toString().trim()); }
            catch(Exception e){ Toast.makeText(this,"Invalid price",Toast.LENGTH_SHORT).show(); return; }

            db.updateProduct(id, newName, newDesc, newPrice, image);
            Toast.makeText(this,"Product updated!",Toast.LENGTH_SHORT).show();
            loadProducts();
        });

        builder.setNegativeButton("Delete", (dialog, which) -> {
            db.deleteProduct(id);
            Toast.makeText(this,"Product deleted!", Toast.LENGTH_SHORT).show();
            loadProducts();
        });

        builder.setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }
}