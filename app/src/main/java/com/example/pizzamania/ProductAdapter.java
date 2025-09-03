package com.example.pizzamania;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ProductAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> productNames;
    ArrayList<String> productDescriptions;
    ArrayList<byte[]> productImages;

    public ProductAdapter(Context context, ArrayList<String> names, ArrayList<String> descriptions, ArrayList<byte[]> images) {
        this.context = context;
        this.productNames = names;
        this.productDescriptions = descriptions;
        this.productImages = images;
    }

    @Override
    public int getCount() {
        return productNames.size();
    }

    @Override
    public Object getItem(int position) {
        return productNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.product_list_item, parent, false);
        }

        ImageView img = convertView.findViewById(R.id.imgItem);
        TextView name = convertView.findViewById(R.id.txtItemName);
        TextView desc = convertView.findViewById(R.id.txtItemDesc);

        name.setText(productNames.get(position));
        desc.setText(productDescriptions.get(position));
        if (productImages.get(position) != null) {
            img.setImageBitmap(BitmapFactory.decodeByteArray(productImages.get(position), 0, productImages.get(position).length));
        }

        return convertView;
    }
}
