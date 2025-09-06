package com.example.pizzamania;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private ArrayList<String> productNames, productDescriptions;
    private ArrayList<byte[]> productImages; // from database
    private ArrayList<Double> smallPrices, mediumPrices, largePrices;
    private OnItemClickListener listener;

    public interface OnItemClickListener { void onItemClick(int position); }
    public void setOnItemClickListener(OnItemClickListener listener) { this.listener = listener; }

    public ProductAdapter(Context context,
                          ArrayList<String> names,
                          ArrayList<String> descriptions,
                          ArrayList<byte[]> images,
                          ArrayList<Double> smallPrices,
                          ArrayList<Double> mediumPrices,
                          ArrayList<Double> largePrices) {
        this.context = context;
        this.productNames = names;
        this.productDescriptions = descriptions;
        this.productImages = images;
        this.smallPrices = smallPrices;
        this.mediumPrices = mediumPrices;
        this.largePrices = largePrices;
    }

    public void setProductNames(ArrayList<String> productNames) {
        this.productNames = productNames;
    }
    public void setProductDescriptions(ArrayList<String> productDescriptions) {
        this.productDescriptions = productDescriptions;
    }
    public void setProductImages(ArrayList<byte[]> productImages) {
        this.productImages = productImages;
    }
    public void setSmallPrices(ArrayList<Double> smallPrices) {
        this.smallPrices = smallPrices;
    }
    public void setMediumPrices(ArrayList<Double> mediumPrices) {
        this.mediumPrices = mediumPrices;
    }
    public void setLargePrices(ArrayList<Double> largePrices) {
        this.largePrices = largePrices;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_list_item, parent, false);
        return new ProductViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.txtName.setText(productNames.get(position));
        holder.txtDesc.setText(productDescriptions.get(position));
        holder.txtPrice.setText("S: Rs " + smallPrices.get(position) +
                " | M: Rs " + mediumPrices.get(position) +
                " | L: Rs " + largePrices.get(position));

        // convert byte[] to bitmap
        if (productImages.get(position) != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(productImages.get(position), 0, productImages.get(position).length);
            holder.imgProduct.setImageBitmap(bitmap);
        } else {
            holder.imgProduct.setImageResource(R.drawable.placeholder); // fallback image
        }
    }

    @Override
    public int getItemCount() { return productNames.size(); }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView txtName, txtDesc, txtPrice;

        public ProductViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgItem);
            txtName = itemView.findViewById(R.id.txtItemName);
            txtDesc = itemView.findViewById(R.id.txtItemDesc);
            txtPrice = itemView.findViewById(R.id.txtItemPrice);

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onItemClick(getAdapterPosition());
            });
        }
    }
}
