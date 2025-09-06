package com.example.pizzamania;

public class CartItem {
    private String name;
    private double price;
    private int quantity;
    private byte[] image;

    public CartItem(String name, double price, int quantity, byte[] image) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public byte[] getImage() { return image; }

    public void setQuantity(int quantity) { this.quantity = quantity; }
}
