package com.example.pizzamania;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "PizzaMania";
    private static final int DB_VERSION = 2;

    public SqliteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // ---------------- Customers ----------------
        String customerTable = "CREATE TABLE customer (" +
                "customer_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "customer_name TEXT, " +
                "email TEXT UNIQUE, " +
                "password TEXT, " +
                "address TEXT)";
        db.execSQL(customerTable);

        // ---------------- Admin ----------------
        String adminTable = "CREATE TABLE admin (" +
                "admin_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT UNIQUE, " +
                "password TEXT)";
        db.execSQL(adminTable);

        // ---------------- Products ----------------
        String productTable = "CREATE TABLE product (" +
                "product_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "description TEXT, " +
                "price REAL, " +
                "image BLOB)";
        db.execSQL(productTable);

        // ---------------- Cart ----------------
        String cartTable = "CREATE TABLE cart (" +
                "cart_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "customer_ID INTEGER, " +
                "product_ID INTEGER, " +
                "quantity INTEGER, " +
                "FOREIGN KEY(customer_ID) REFERENCES customer(customer_ID), " +
                "FOREIGN KEY(product_ID) REFERENCES product(product_ID))";
        db.execSQL(cartTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS customer");
        db.execSQL("DROP TABLE IF EXISTS admin");
        db.execSQL("DROP TABLE IF EXISTS product");
        db.execSQL("DROP TABLE IF EXISTS cart");
        onCreate(db);
    }

    // ---------------- Customers ----------------
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM customer WHERE email=? AND password=?",
                new String[]{email, password});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    // ---------------- Admin ----------------
    public boolean checkAdmin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM admin WHERE username=? AND password=?",
                new String[]{username, password});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    // ---------------- Products ----------------
    // Add product
    public long insertProduct(String name, String description, double price, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        values.put("price", price);
        values.put("image", image); // store image as BLOB
        long result = db.insert("product", null, values);
        db.close();
        return result;
    }

    // Get all products
    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM product", null);
    }

    // Get single product by ID
    public Cursor getProductById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM product WHERE product_ID=?", new String[]{String.valueOf(id)});
    }

    // Update product
    public int updateProduct(int id, String name, String description, double price, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        values.put("price", price);
        values.put("image", image);
        int rows = db.update("product", values, "product_ID=?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }

    // Delete product
    public int deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete("product", "product_ID=?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }

    // ---------------- Cart ----------------
    public long addToCart(int customerId, int productId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("customer_ID", customerId);
        values.put("product_ID", productId);
        values.put("quantity", quantity);
        long result = db.insert("cart", null, values);
        db.close();
        return result;
    }

    public Cursor getCartItems(int customerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT c.cart_ID, p.name, p.price, c.quantity, p.image " +
                        "FROM cart c JOIN product p ON c.product_ID = p.product_ID " +
                        "WHERE c.customer_ID = ?",
                new String[]{String.valueOf(customerId)}
        );
    }

    public void clearCart(int customerId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("cart", "customer_ID=?", new String[]{String.valueOf(customerId)});
        db.close();
    }
}
