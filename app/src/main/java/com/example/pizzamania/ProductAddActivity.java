package com.example.pizzamania;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;

public class ProductAddActivity extends AppCompatActivity {

    EditText editName, editDesc, editPrice;
    Button btnChooseImage, btnAdd, btnViewProducts;
    ImageView preview;
    Bitmap selectedBitmap;
    SqliteHelper db;

    // Request permission launcher
    private final ActivityResultLauncher<String> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> {
                if (granted) openGallery();
                else Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            });

    // Image picker launcher (GetContent)
    private final ActivityResultLauncher<String> imagePicker =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {

                if (uri != null) handlePickedImage(uri);
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_add);

        editName = findViewById(R.id.editName);
        editDesc = findViewById(R.id.editDesc);
        editPrice = findViewById(R.id.editPrice);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        btnAdd = findViewById(R.id.btnAdd);
        btnViewProducts = findViewById(R.id.btnViewProducts);
        preview = findViewById(R.id.productImage);

        db = new SqliteHelper(this);

        btnChooseImage.setOnClickListener(v -> checkPermissionAndPick());


            btnAdd.setOnClickListener(v -> addProduct());


            btnViewProducts.setOnClickListener(v -> {
                Intent intent = new Intent(ProductAddActivity.this, ProductList.class);
                startActivity(intent);
        });
    }

    private void checkPermissionAndPick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ permission name READ_MEDIA_IMAGES
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }

    private void openGallery() {
        imagePicker.launch("image/*");
    }

    private void handlePickedImage(Uri uri) {
        try {
            selectedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            preview.setImageBitmap(selectedBitmap);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
        }
    }

        private void addProduct() {
            String name = editName.getText().toString().trim();
            String desc = editDesc.getText().toString().trim();
            String priceStr = editPrice.getText().toString().trim();

            if (name.isEmpty() || priceStr.isEmpty() || selectedBitmap == null) {
                Toast.makeText(this, "Please fill name, price and choose image", Toast.LENGTH_SHORT).show();
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceStr);
            } catch (NumberFormatException ex) {
                Toast.makeText(this, "Enter valid price", Toast.LENGTH_SHORT).show();
                return;
            }

            byte[] imgBytes = bitmapToBytes(selectedBitmap);

            long inserted = db.insertProduct(name, desc, price, imgBytes);

            if (inserted != -1) {
                Toast.makeText(this, "Product added successfully!", Toast.LENGTH_SHORT).show();
                clearFields();
            } else {
                Toast.makeText(this, "Error adding product", Toast.LENGTH_SHORT).show();
            }
        }


        private byte[] bitmapToBytes(Bitmap bmp) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 85, bos);
        return bos.toByteArray();
    }

        private void clearFields() {
            editName.setText("");
            editDesc.setText("");
            editPrice.setText("");
            preview.setImageBitmap(null);
            selectedBitmap = null;
        }
}
