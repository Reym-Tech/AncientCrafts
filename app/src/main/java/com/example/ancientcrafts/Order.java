package com.example.ancientcrafts;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Order extends AppCompatActivity {

    private TextView deliveryAddress, checkoutProductName, checkoutProductPrice, subtotalText, shippingText, totalText, checkoutTotalPrice;
    private ImageView checkoutProductImage;
    private Button btnPlaceOrder;

    // Simulated data (replace this with real data from Intent or database)
    private String productName = "Vintage Wooden Cabinet";
    private double productPrice = 2999.00;
    private double shippingFee = 40.00;
    private String fullAddress = "John Doe\n123 Street Name, Barangay, City, Province";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity); // Make sure your XML file is named activity_order.xml or adjust this accordingly

        // Initialize views
        deliveryAddress = findViewById(R.id.deliveryAddress);
        checkoutProductName = findViewById(R.id.checkoutProductName);
        checkoutProductPrice = findViewById(R.id.checkoutProductPrice);
        subtotalText = findViewById(R.id.subtotalText);
        shippingText = findViewById(R.id.shippingText);
        totalText = findViewById(R.id.totalText);
        checkoutTotalPrice = findViewById(R.id.checkoutTotalPrice);
        checkoutProductImage = findViewById(R.id.checkoutProductImage);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);

        // Populate data
        deliveryAddress.setText(fullAddress);
        checkoutProductName.setText(productName);
        checkoutProductPrice.setText("₱" + String.format("%.2f", productPrice));
        subtotalText.setText("₱" + String.format("%.2f", productPrice));
        shippingText.setText("₱" + String.format("%.2f", shippingFee));
        double total = productPrice + shippingFee;
        totalText.setText("₱" + String.format("%.2f", total));
        checkoutTotalPrice.setText("₱" + String.format("%.2f", total));

        // Optional: Set a placeholder image (adjust with actual product if needed)
        checkoutProductImage.setImageResource(R.drawable.pngtree_ornately_carved_wooden_cabinet_featuring_two_doors_with_arch_designs_open_png_image_15890166);

        // Button action
        btnPlaceOrder.setOnClickListener(v -> {
            Toast.makeText(Order.this, "Your order has been placed!", Toast.LENGTH_SHORT).show();
            // You can add further logic here such as uploading order to Firebase or navigating to another activity
        });
    }
}