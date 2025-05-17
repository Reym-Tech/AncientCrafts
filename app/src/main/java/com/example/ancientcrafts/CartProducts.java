package com.example.ancientcrafts;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ancientcrafts.models.Product;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class CartProducts extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_products_activity);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        LinearLayout cartListLayout = findViewById(R.id.cart_product_list_layout);
        List<Product> cartItems = CartManager.getInstance().getCartItems();

        LayoutInflater inflater = LayoutInflater.from(this);

        for (Product product : cartItems) {
            View itemView = inflater.inflate(R.layout.cart_item, cartListLayout, false);

            ImageView imageView = itemView.findViewById(R.id.cart_item_image);
            TextView nameView = itemView.findViewById(R.id.cart_item_name);
            TextView priceView = itemView.findViewById(R.id.cart_item_price);

            nameView.setText(product.getName());
            priceView.setText(String.format("₱%.2f", product.getPrice()));

            Glide.with(this)
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_img)
                    .into(imageView);

            cartListLayout.addView(itemView);
        }
    }
}
