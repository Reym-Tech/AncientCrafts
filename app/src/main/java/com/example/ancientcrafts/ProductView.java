package com.example.ancientcrafts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ancientcrafts.models.Product;
import com.google.firebase.database.annotations.Nullable;

public class ProductView extends AppCompatActivity {
    private ImageView productImage;
    private TextView productName, productDiscPrice, productDescription;

    private Button addToCartBtn, buyNowBtn;
    private ImageView favBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view); // Use your actual XML filename here

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Initialize views
        productImage = findViewById(R.id.product_picture); // Make sure to initialize this
        productName = findViewById(R.id.product_name); // Initialize productName
        productDiscPrice = findViewById(R.id.product_discPrice);
        productDescription = findViewById(R.id.product_description);

        addToCartBtn = findViewById(R.id.CartBtn);
        buyNowBtn = findViewById(R.id.buyNow_Btn);
        favBtn = findViewById(R.id.favBtn);

        // Get the Product object passed from the previous activity
        Product product = getIntent().getParcelableExtra("product");

        if (product != null) {
            // Populate views with product data
            productName.setText(product.getName());
            productDiscPrice.setText(String.format("₱%.2f", product.getPrice()));
            productDescription.setText(product.getDescription());

            // Load product image with Glide
            Glide.with(this)
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.placeholder_image) // Replace with your placeholder drawable
                    .error(R.drawable.error_img) // Replace with your error drawable
                    .into(productImage);

            // Add to Cart button
            addToCartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CartManager.getInstance().addToCart(product);
                    Toast.makeText(ProductView.this, product.getName() + " added to cart!", Toast.LENGTH_SHORT).show();

                    // Go to Cart Page
                    Intent intent = new Intent(ProductView.this, CartProducts.class);
                    startActivity(intent);
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

        } else {
            Toast.makeText(this, "Product data not available", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if no product data received
        }
    }
}
