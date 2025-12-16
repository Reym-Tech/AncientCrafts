package com.example.ancientcrafts.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ancientcrafts.R;
import com.example.ancientcrafts.models.Product;
import com.example.ancientcrafts.ProductView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final List<Product> productList;
    private final Context context;
    private final int placeholderImageResId;

    public ProductAdapter(List<Product> productList, Context context, int placeholderImageResId) {
        this.productList = productList;
        this.context = context;
        this.placeholderImageResId = placeholderImageResId;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.productName.setText(product.getName());
        holder.productPrice.setText("₱" + String.format("%.2f", product.getPrice()));
        holder.productSoldCount.setText("Sold: " + product.getSoldCount()); // updated here

        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(placeholderImageResId)
                .error(placeholderImageResId)
                .into(holder.productImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductView.class);
            intent.putExtra("product", product); // Passing Parcelable
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, productSoldCount; // renamed here

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productSoldCount = itemView.findViewById(R.id.product_sold); // renamed here
        }
    }
}
