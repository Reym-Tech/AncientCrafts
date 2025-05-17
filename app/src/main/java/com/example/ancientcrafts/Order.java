package com.example.ancientcrafts;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ancientcrafts.models.Product;

public class Order extends AppCompatActivity {

    private TextView deliveryAddress, productName, productPrice, subtotalText, shippingText, totalText, checkoutTotalPrice;
    private ImageView productImage;
    private EditText voucherCodeInput;
    private Button applyVoucherBtn, btnPlaceOrder;
    private RadioGroup paymentMethodGroup;

    private double shippingFee = 40.00; // default shipping fee

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Bind views from XML
        deliveryAddress = findViewById(R.id.deliveryAddress);
        productName = findViewById(R.id.checkoutProductName);
        productPrice = findViewById(R.id.checkoutProductPrice);
        subtotalText = findViewById(R.id.subtotalText);
        shippingText = findViewById(R.id.shippingText);
        totalText = findViewById(R.id.totalText);
        checkoutTotalPrice = findViewById(R.id.checkoutTotalPrice);
        productImage = findViewById(R.id.checkoutProductImage);
        voucherCodeInput = findViewById(R.id.voucherCodeInput);
        applyVoucherBtn = findViewById(R.id.applyVoucherBtn);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        paymentMethodGroup = findViewById(R.id.paymentMethodGroup);

        // Get the product from the Intent
        Product product = getIntent().getParcelableExtra("product");

        if (product != null) {
            // Load product data into UI
            productName.setText(product.getName());
            productPrice.setText(String.format("₱%.2f", product.getPrice()));
            subtotalText.setText(String.format("₱%.2f", product.getPrice()));
            shippingText.setText(String.format("₱%.2f", shippingFee));
            double total = product.getPrice() + shippingFee;
            totalText.setText(String.format("₱%.2f", total));
            checkoutTotalPrice.setText(String.format("₱%.2f", total));

            Glide.with(this)
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_img)
                    .into(productImage);
        } else {
            Toast.makeText(this, "No product data received", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Handle voucher button click
        applyVoucherBtn.setOnClickListener(v -> {
            String voucher = voucherCodeInput.getText().toString().trim();
            if (voucher.equalsIgnoreCase("DISCOUNT10")) {
                double discount = 10.00;
                double subtotal = product.getPrice();
                double total = subtotal + shippingFee - discount;
                totalText.setText(String.format("₱%.2f", total));
                checkoutTotalPrice.setText(String.format("₱%.2f", total));
                Toast.makeText(Order.this, "Voucher applied!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Order.this, "Invalid voucher code", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle place order button
        btnPlaceOrder.setOnClickListener(v -> {
            int selectedId = paymentMethodGroup.getCheckedRadioButtonId();
            RadioButton selectedMethod = findViewById(selectedId);
            String paymentMethod = selectedMethod.getText().toString();

            Toast.makeText(Order.this, "Order placed using " + paymentMethod, Toast.LENGTH_LONG).show();
            // Proceed to confirmation or backend logic
        });
    }
}
