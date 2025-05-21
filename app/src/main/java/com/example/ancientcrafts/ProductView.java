package com.example.ancientcrafts;

import android.os.Bundle;
import android.view.WindowManager;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view); // Use your actual XML filename here

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Initialize views
        productImage = findViewById(R.id.product_picture);
        productName = findViewById(R.id.product_name);
        productDiscPrice = findViewById(R.id.product_discPrice);
        productDescription = findViewById(R.id.product_description);

        // Get the Product object passed from the previous Activity
        Product product = getIntent().getParcelableExtra("product");

        if (product != null) {
            // Populate views with product data
            productName.setText(product.getName());
            productDiscPrice.setText(String.format("₱%.2f", product.getPrice()));
            productDescription.setText(product.getDescription());

            // Load product image with Glide
            Glide.with(this)
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.placeholder_image)  // Replace with your placeholder drawable
                    .error(R.drawable.error_img)              // Replace with your error drawable
                    .into(productImage);

        } else {
            Toast.makeText(this, "Product data not available", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if no product data received
        }
    }
}
