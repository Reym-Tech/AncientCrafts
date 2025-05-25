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

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;
    private int placeholderImageResId;

    public ProductAdapter(List<Product> productList, Context context, int placeholderImageResId) {
        this.productList = productList;
        this.context = context;
        this.placeholderImageResId = R.drawable.placeholder_image;
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

        holder.nameTextView.setText(product.getName());
        holder.priceTextView.setText("₱" + String.format("%.2f", product.getPrice()));

        String imageUrl = product.getImageUrl();
        Glide.with(context)
                .load(imageUrl)
                .placeholder(placeholderImageResId)
                .error(placeholderImageResId)
                .into(holder.productImage);

        // 👇 Click-to-view full product
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, com.example.ancientcrafts.ProductView.class);
            intent.putExtra("product", product); // Pass the whole Product object
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, priceTextView;
        ImageView productImage;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.product_name);
            priceTextView = itemView.findViewById(R.id.product_price);
            productImage = itemView.findViewById(R.id.product_image);
        }
    }
}
