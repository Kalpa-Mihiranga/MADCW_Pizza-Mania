package com.example.pizzamania;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CartAdapter extends ArrayAdapter<CartItem> {

    public CartAdapter(@NonNull Context context, ArrayList<CartItem> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_cart_item, parent, false);

        }

        CartItem item = getItem(position);
        TextView txtName = convertView.findViewById(R.id.txtItemName);
        TextView txtPrice = convertView.findViewById(R.id.txtItemPrice);
        Button btnRemove = convertView.findViewById(R.id.btnRemove);

        txtName.setText(item.getName());
        txtPrice.setText("Rs. " + item.getPrice());

        btnRemove.setOnClickListener(v -> {
            CartManager.getInstance().removeItem(position);
            notifyDataSetChanged(); // Refresh the list
        });

        return convertView;
    }
}
