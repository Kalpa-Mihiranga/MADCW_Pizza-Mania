package com.example.pizzamania;

public class Product {
    public int id;
    public String name;
    public String description;
    public double price;
    public byte[] image;

    public Product(int id, String name, String description, double price, byte[] image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
    }
}
