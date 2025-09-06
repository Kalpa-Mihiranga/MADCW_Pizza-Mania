package com.example.pizzamania;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CartAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CartItem> cartItems;

    public CartAdapter(Context context, ArrayList<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @Override
    public int getCount() {
        return cartItems.size();
    }

    @Override
    public Object getItem(int position) {
        return cartItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_cart_item, parent, false);
            holder = new ViewHolder();
            holder.imgItemImage = convertView.findViewById(R.id.imgItemImage);
            holder.txtItemName = convertView.findViewById(R.id.txtItemName);
            holder.txtItemPrice = convertView.findViewById(R.id.txtItemPrice);
            holder.txtQuantity = convertView.findViewById(R.id.txtQuantity);
            holder.btnIncrease = convertView.findViewById(R.id.btnIncrease);
            holder.btnDecrease = convertView.findViewById(R.id.btnDecrease);
            holder.btnRemove = convertView.findViewById(R.id.btnRemove);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CartItem item = cartItems.get(position);

        holder.txtItemName.setText(item.getName());
        holder.txtItemPrice.setText("Rs. " + item.getPrice());
        holder.txtQuantity.setText(String.valueOf(item.getQuantity()));

        // Show image
        if (item.getImage() != null && item.getImage().length > 0) {
            holder.imgItemImage.setImageBitmap(BitmapFactory.decodeByteArray(item.getImage(), 0, item.getImage().length));
        } else {
            holder.imgItemImage.setImageResource(android.R.drawable.ic_menu_gallery); // fallback image
        }

        // Increase quantity
        holder.btnIncrease.setOnClickListener(v -> {
            int qty = item.getQuantity();
            item.setQuantity(qty + 1);
            notifyDataSetChanged();
        });

        // Decrease quantity
        holder.btnDecrease.setOnClickListener(v -> {
            int qty = item.getQuantity();
            if (qty > 1) {
                item.setQuantity(qty - 1);
                notifyDataSetChanged();
            }
        });

        // Remove item
        holder.btnRemove.setOnClickListener(v -> {
            cartItems.remove(position);
            notifyDataSetChanged();
        });

        return convertView;
    }

    static class ViewHolder {
        ImageView imgItemImage;
        TextView txtItemName, txtItemPrice, txtQuantity;
        Button btnIncrease, btnDecrease, btnRemove;
    }
}
