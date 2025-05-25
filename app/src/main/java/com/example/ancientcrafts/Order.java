package com.example.ancientcrafts;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.ancientcrafts.models.Notification;
import com.example.ancientcrafts.models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Order extends AppCompatActivity {

    private TextView deliveryAddress, productName, productPrice,
            subtotalText, shippingText, totalText, checkoutTotalPrice;
    private ImageView productImage;
    private EditText voucherCodeInput;
    private Button applyVoucherBtn, btnPlaceOrder;
    private RadioGroup paymentMethodGroup;
    private CardView cardDeliveryAddress;

    private double shippingFee = 40.00;
    private double discount = 0.0;
    private double totalPrice = 0.0;

    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
        cardDeliveryAddress = findViewById(R.id.cardDeliveryAddress);

        cardDeliveryAddress.setOnClickListener(v -> {
            ChangeAddressBottomSheet sheet = new ChangeAddressBottomSheet();
            sheet.setListener(addr -> deliveryAddress.setText(addr.getFullAddress()));
            sheet.show(getSupportFragmentManager(), "addrSheet");
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(user.getUid());

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snap) {
                    if (snap.exists()) {
                        String name = snap.child("first_name").getValue(String.class);
                        String phone = snap.child("phone_number").getValue(String.class);
                        String addr = snap.child("address").getValue(String.class);

                        String fullAddress = name + "\n" + addr + "\nContact: " + phone;
                        deliveryAddress.setText(fullAddress);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError e) {
                    Toast.makeText(Order.this, "Failed to load address", Toast.LENGTH_SHORT).show();
                }
            });
        }

        product = getIntent().getParcelableExtra("product");
        if (product != null) {
            productName.setText(product.getName());
            productPrice.setText(String.format("₱%.2f", product.getPrice()));
            subtotalText.setText(String.format("₱%.2f", product.getPrice()));
            shippingText.setText(String.format("₱%.2f", shippingFee));

            totalPrice = product.getPrice() + shippingFee;
            totalText.setText(String.format("₱%.2f", totalPrice));
            checkoutTotalPrice.setText(String.format("₱%.2f", totalPrice));

            Glide.with(this)
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_img)
                    .into(productImage);
        } else {
            Toast.makeText(this, "No product data received", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        applyVoucherBtn.setOnClickListener(v -> {
            String voucher = voucherCodeInput.getText().toString().trim();
            if (voucher.equalsIgnoreCase("DISCOUNT10")) {
                discount = 10.00;
                totalPrice = product.getPrice() + shippingFee - discount;
                totalText.setText(String.format("₱%.2f", totalPrice));
                checkoutTotalPrice.setText(String.format("₱%.2f", totalPrice));
                Toast.makeText(Order.this, "Voucher applied!", Toast.LENGTH_SHORT).show();
            } else {
                discount = 0.0;
                Toast.makeText(Order.this, "Invalid voucher code", Toast.LENGTH_SHORT).show();
            }
        });

        btnPlaceOrder.setOnClickListener(v -> {
            int id = paymentMethodGroup.getCheckedRadioButtonId();
            btnPlaceOrder.setEnabled(false);
            btnPlaceOrder.postDelayed(() -> btnPlaceOrder.setEnabled(true), 3000);

            if (id != -1) {
                String method = ((RadioButton) findViewById(id)).getText().toString();
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser == null) {
                    Toast.makeText(Order.this, "User not logged in", Toast.LENGTH_SHORT).show();
                    return;
                }

                String buyerUid = currentUser.getUid();
                String buyerName = currentUser.getDisplayName() != null ? currentUser.getDisplayName() : "Someone";
                String deliveryAddr = deliveryAddress.getText().toString();

                // ✅ Get seller UID from product
                String sellerUid = product.getSellerUid();
                if (sellerUid == null || sellerUid.isEmpty()) {
                    Toast.makeText(Order.this, "Seller ID missing. Cannot place order.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // ✅ Prepare order map
                HashMap<String, Object> orderMap = new HashMap<>();
                orderMap.put("productId", product.getId());
                orderMap.put("productName", product.getName());
                orderMap.put("productPrice", product.getPrice());
                orderMap.put("buyerUid", buyerUid);
                orderMap.put("buyerName", buyerName);
                orderMap.put("deliveryAddress", deliveryAddr);
                orderMap.put("paymentMethod", method);
                orderMap.put("totalPrice", totalPrice);
                orderMap.put("discount", discount);
                orderMap.put("timestamp", System.currentTimeMillis());
                orderMap.put("productImageUrl", product.getImageUrl());
                orderMap.put("sellerApproval", "pending");
                orderMap.put("orderStatus", "toPay");
                orderMap.put("quantity", 1);

                // ✅ Include seller UID (required for filtering later!)
                orderMap.put("sellerUid", sellerUid);

                // ✅ Save order to Firebase
                DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");
                String orderId = ordersRef.push().getKey();
                orderMap.put("orderId", orderId);

                if (orderId != null) {
                    ordersRef.child(orderId).setValue(orderMap)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(Order.this, "Order placed successfully!", Toast.LENGTH_SHORT).show();

                                // ✅ Send notification to seller
                                DatabaseReference notifRef = FirebaseDatabase.getInstance()
                                        .getReference("notifications")
                                        .child(sellerUid);

                                Notification notification = new Notification(buyerName, product.getName());
                                notifRef.push().setValue(notification);

                                finish();
                            })
                            .addOnFailureListener(e -> Toast.makeText(Order.this, "Failed to place order: " + e.getMessage(), Toast.LENGTH_LONG).show());
                } else {
                    Toast.makeText(Order.this, "Failed to generate order ID", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Order.this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
