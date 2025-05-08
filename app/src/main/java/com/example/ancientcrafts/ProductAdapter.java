package com.example.ancientcrafts;

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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.ancientcrafts.models.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private final List<Product> productList;
    private final Context context;
    private final int placeholderResId;

    public ProductAdapter(List<Product> productList, Context context, int placeholderResId) {
        this.productList = productList != null ? productList : new ArrayList<>();
        this.context = context;
        this.placeholderResId = placeholderResId;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        if (product == null) return;

        try {
            // Set product data with null checks
            holder.productName.setText(product.getName() != null ? product.getName() : "");

            holder.productPrice.setText(product.getPrice() >= 0 ?
                    String.format(Locale.getDefault(), "₱%.2f", product.getPrice()) : "₱--");

            holder.productOriginalPrice.setText(product.getOriginalPrice() >= 0 ?
                    String.format(Locale.getDefault(), "₱%.2f", product.getOriginalPrice()) : "");

            holder.productSoldCount.setText(product.getSoldCount() >= 0 ?
                    String.format(Locale.getDefault(), "%,d sold", product.getSoldCount()) : "");

            holder.productRating.setText(product.getRating() >= 0 ?
                    String.format(Locale.getDefault(), "%.1f", product.getRating()) : "-.-");

            holder.productReviewCount.setText(product.getReviewCount() >= 0 ?
                    String.format(Locale.getDefault(), "(%,d)", product.getReviewCount()) : "");

            // Load image with error handling
            if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                Glide.with(context)
                        .load(product.getImageUrl())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .placeholder(placeholderResId)
                        .error(placeholderResId)
                        .into(holder.productImage);
            } else {
                holder.productImage.setImageResource(placeholderResId);
            }

            // Set click listener
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, ProductView.class);
                intent.putExtra("product", product);
                context.startActivity(intent);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void updateData(List<Product> newProducts) {
        productList.clear();
        productList.addAll(newProducts != null ? newProducts : new ArrayList<>());
        notifyDataSetChanged();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        final ImageView productImage;
        final TextView productName;
        final TextView productPrice;
        final TextView productOriginalPrice;
        final TextView productSoldCount;
        final TextView productRating;
        final TextView productReviewCount;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productOriginalPrice = itemView.findViewById(R.id.product_original_price);
            productSoldCount = itemView.findViewById(R.id.product_sold_count);
            productRating = itemView.findViewById(R.id.product_rating);
            productReviewCount = itemView.findViewById(R.id.product_review_count);
        }
    }
}