package com.example.pizzamania;

import android.content.Context;
import android.graphics.Bitmap;
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
    ArrayList<String> names, prices;
    ArrayList<byte[]> images;

    public ProductAdapter(Context context, ArrayList<String> names, ArrayList<String> prices, ArrayList<byte[]> images) {
        this.context = context;
        this.names = names;
        this.prices = prices;
        this.images = images;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return names.get(position);
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

        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvPrice = convertView.findViewById(R.id.tvPrice);
        ImageView ivProduct = convertView.findViewById(R.id.ivProduct);

        tvName.setText(names.get(position));
        tvPrice.setText("Rs. " + prices.get(position));

        byte[] imgBytes = images.get(position);
        if (imgBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
            ivProduct.setImageBitmap(bitmap);
        }

        return convertView;
    }
}