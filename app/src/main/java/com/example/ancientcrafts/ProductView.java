package com.example.ancientcrafts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ancientcrafts.models.Product;

public class ProductView extends AppCompatActivity {

    private ImageView productImage;
    private TextView productName, productDiscPrice, productDescription;

    private Button addToCartBtn, buyNowBtn;
    private ImageView favBtn, cartBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Initialize views
        productImage = findViewById(R.id.product_picture);
        productName = findViewById(R.id.product_name);
        productDiscPrice = findViewById(R.id.product_discPrice);
        productDescription = findViewById(R.id.product_description);

        addToCartBtn = findViewById(R.id.CartBtn);
        buyNowBtn = findViewById(R.id.buyNow_Btn);
        favBtn = findViewById(R.id.favBtn);
        cartBtn = findViewById(R.id.cartBtn);

        // Get the Product object passed from previous activity
        Product product = getIntent().getParcelableExtra("product");

        if (product != null) {
            // Populate UI
            productName.setText(product.getName());
            productDiscPrice.setText(String.format("₱%.2f", product.getPrice()));
            productDescription.setText(product.getDescription());

            Glide.with(this)
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_img)
                    .into(productImage);

            // Add to Cart button
            addToCartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: Add your add-to-cart logic here (e.g., local DB or Firebase)
                    Toast.makeText(ProductView.this, product.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
                }
            });

            // Buy Now button
            buyNowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProductView.this, Order.class);
                    intent.putExtra("product", product);
                    startActivity(intent);
                }
            });

            // Optional: Favorite button click (like or wishlist)
            favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: Add to favorites logic
                    Toast.makeText(ProductView.this, "Added to favorites!", Toast.LENGTH_SHORT).show();
                }
            });

            // Optional: Go to Cart page
            cartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: Navigate to Cart Activity
                    Toast.makeText(ProductView.this, "Opening cart...", Toast.LENGTH_SHORT).show();
                    // startActivity(new Intent(ProductView.this, CartActivity.class));
                }
            });

        } else {
            Toast.makeText(this, "Product data not available", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
