package com.example.pizzamania;

import java.util.ArrayList;

public class CartManager {
    private static CartManager instance;
    private ArrayList<CartItem> cartItems = new ArrayList<>();

    private CartManager() {}

    public static CartManager getInstance() {
        if (instance == null) instance = new CartManager();
        return instance;
    }

    public void addItem(String name, double price, int quantity, byte[] image) {
        cartItems.add(new CartItem(name, price, quantity, image));
    }

    public void removeItem(int position) {
        if (position >= 0 && position < cartItems.size()) cartItems.remove(position);
    }

    public ArrayList<CartItem> getCartItems() { return cartItems; }

    public double getTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) total += item.getPrice() * item.getQuantity();
        return total;
    }

    public void clearCart() { cartItems.clear(); }
}
