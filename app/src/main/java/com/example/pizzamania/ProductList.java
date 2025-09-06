package com.example.pizzamania;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView;
import android.view.View;
import android.widget.BaseAdapter;
import android.view.LayoutInflater;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.ViewGroup;

public class ProductList extends AppCompatActivity {

    ListView listView;
    Spinner branchSpinner;
    SqliteHelper db;
    ArrayList<ProductItem> productItems;
    String selectedBranch = "Colombo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        listView = findViewById(R.id.listViewProducts);
        branchSpinner = findViewById(R.id.branchSpinner);
        db = new SqliteHelper(this);

        // Setup branch spinner
        ArrayAdapter<String> branchAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Colombo", "Galle"});
        branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branchSpinner.setAdapter(branchAdapter);

        branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBranch = branchSpinner.getSelectedItem().toString();
                loadProducts();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        loadProducts();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            int productId = productItems.get(position).id;
            showEditDeleteDialog(productId);
        });
    }

    private void loadProducts() {
        productItems = new ArrayList<>();
        var cursor = db.getProductsByBranch(selectedBranch);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("product_ID"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                double smallPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("smallPrice"));
                double mediumPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("mediumPrice"));
                double largePrice = cursor.getDouble(cursor.getColumnIndexOrThrow("largePrice"));
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
                productItems.add(new ProductItem(id, name, desc, smallPrice, mediumPrice, largePrice, image));
            } while (cursor.moveToNext());
        }
        cursor.close();

        ProductListAdapter adapter = new ProductListAdapter(this, productItems);
        listView.setAdapter(adapter);
    }

    private void showEditDeleteDialog(int productId) {
        var cursor = db.getProductById(productId);
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String desc = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            double smallPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("smallPrice"));
            double mediumPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("mediumPrice"));
            double largePrice = cursor.getDouble(cursor.getColumnIndexOrThrow("largePrice"));
            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
            String branch = cursor.getString(cursor.getColumnIndexOrThrow("branch"));
            showEditDialog(productId, name, desc, smallPrice, mediumPrice, largePrice, image, branch);
        }
        cursor.close();
    }

    private void showEditDialog(int id, String name, String desc,
                                double smallPrice, double mediumPrice, double largePrice, byte[] image, String branch) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit/Delete Product");

        EditText etName = new EditText(this);
        etName.setHint("Name");
        etName.setText(name);

        EditText etDesc = new EditText(this);
        etDesc.setHint("Description");
        etDesc.setText(desc);

        EditText etSmallPrice = new EditText(this);
        etSmallPrice.setHint("Small Price");
        etSmallPrice.setText(String.valueOf(smallPrice));

        EditText etMediumPrice = new EditText(this);
        etMediumPrice.setHint("Medium Price");
        etMediumPrice.setText(String.valueOf(mediumPrice));

        EditText etLargePrice = new EditText(this);
        etLargePrice.setHint("Large Price");
        etLargePrice.setText(String.valueOf(largePrice));

        Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
        ImageView iv = new ImageView(this);
        iv.setImageBitmap(bmp);

        // Branch spinner for editing
        Spinner editBranchSpinner = new Spinner(this);
        ArrayAdapter<String> branchAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Colombo", "Galle"});
        branchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editBranchSpinner.setAdapter(branchAdapter);
        int branchPos = branch.equals("Galle") ? 1 : 0;
        editBranchSpinner.setSelection(branchPos);

        // Layout
        var layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.addView(etName);
        layout.addView(etDesc);
        layout.addView(etSmallPrice);
        layout.addView(etMediumPrice);
        layout.addView(etLargePrice);
        layout.addView(iv);
        layout.addView(editBranchSpinner);

        builder.setView(layout);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String newName = etName.getText().toString().trim();
            String newDesc = etDesc.getText().toString().trim();

            double newSmall, newMedium, newLarge;
            try {
                newSmall = Double.parseDouble(etSmallPrice.getText().toString().trim());
                newMedium = Double.parseDouble(etMediumPrice.getText().toString().trim());
                newLarge = Double.parseDouble(etLargePrice.getText().toString().trim());
            } catch (Exception e) {
                Toast.makeText(this, "Invalid price(s)", Toast.LENGTH_SHORT).show();
                return;
            }

            String newBranch = editBranchSpinner.getSelectedItem().toString();
            db.updateProduct(id, newName, newDesc, newSmall, newMedium, newLarge, image, newBranch);
            Toast.makeText(this, "Product updated!", Toast.LENGTH_SHORT).show();
            loadProducts();
        });

        builder.setNegativeButton("Delete", (dialog, which) -> {
            db.deleteProduct(id);
            Toast.makeText(this, "Product deleted!", Toast.LENGTH_SHORT).show();
            loadProducts();
        });

        builder.setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    // Helper class for product item
    static class ProductItem {
        int id;
        String name, desc;
        double smallPrice, mediumPrice, largePrice;
        byte[] image;
        ProductItem(int id, String name, String desc, double small, double medium, double large, byte[] image) {
            this.id = id; this.name = name; this.desc = desc;
            this.smallPrice = small; this.mediumPrice = medium; this.largePrice = large;
            this.image = image;
        }
    }

    // Custom adapter for product list with image and nice design
    static class ProductListAdapter extends BaseAdapter {
        private final List<ProductItem> items;
        private final LayoutInflater inflater;
        ProductListAdapter(android.content.Context ctx, List<ProductItem> items) {
            this.items = items;
            this.inflater = LayoutInflater.from(ctx);
        }
        @Override public int getCount() { return items.size(); }
        @Override public Object getItem(int i) { return items.get(i); }
        @Override public long getItemId(int i) { return items.get(i).id; }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = inflater.inflate(R.layout.item_product_list, parent, false);

            ProductItem item = items.get(position);

            ImageView img = convertView.findViewById(R.id.imgProduct);
            TextView name = convertView.findViewById(R.id.txtProductName);
            TextView desc = convertView.findViewById(R.id.txtProductDesc);
            TextView price = convertView.findViewById(R.id.txtProductPrice);

            if (item.image != null && item.image.length > 0)
                img.setImageBitmap(BitmapFactory.decodeByteArray(item.image, 0, item.image.length));
            else
                img.setImageResource(android.R.drawable.ic_menu_gallery);

            name.setText(item.name);
            desc.setText(item.desc);
            price.setText("Small: Rs. " + item.smallPrice + " | Medium: Rs. " + item.mediumPrice + " | Large: Rs. " + item.largePrice);

            return convertView;
        }
    }
}
