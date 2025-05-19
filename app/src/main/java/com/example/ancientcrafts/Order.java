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

import com.example.ancientcrafts.models.Notification;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import com.bumptech.glide.Glide;
import com.example.ancientcrafts.models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Order extends AppCompatActivity {

    private TextView deliveryAddress, productName, productPrice,
            subtotalText, shippingText, totalText, checkoutTotalPrice;
    private ImageView productImage;
    private EditText voucherCodeInput;
    private Button applyVoucherBtn, btnPlaceOrder;
    private RadioGroup paymentMethodGroup;
    private CardView cardDeliveryAddress;

    private double shippingFee = 40.00;      // default shipping fee

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /* ---------- bind views ---------- */
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
        cardDeliveryAddress = findViewById(R.id.cardDeliveryAddress);   // NEW

        /* ---------- bottom‑sheet pop‑up for address ---------- */
        cardDeliveryAddress.setOnClickListener(v -> {                    // NEW
            ChangeAddressBottomSheet sheet = new ChangeAddressBottomSheet();
            sheet.setListener(addr -> {
                deliveryAddress.setText(addr.getFullAddress());
                /* If shipping varies per address, recalc totals here */
            });
            sheet.show(getSupportFragmentManager(), "addrSheet");
        });
        /* ----------------------------------------------------- */

        /* ---------- load default address from Firebase ---------- */
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
        /* -------------------------------------------------------- */

        /* ---------- load product passed in Intent ---------- */
        Product product = getIntent().getParcelableExtra("product");
        if (product != null) {
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
            return;
        }
        /* ---------------------------------------------------- */

        /* ---------- voucher button ---------- */
        applyVoucherBtn.setOnClickListener(v -> {
            String voucher = voucherCodeInput.getText().toString().trim();
            if (voucher.equalsIgnoreCase("DISCOUNT10")) {
                double discount = 10.00;
                double total = product.getPrice() + shippingFee - discount;
                totalText.setText(String.format("₱%.2f", total));
                checkoutTotalPrice.setText(String.format("₱%.2f", total));
                Toast.makeText(Order.this, "Voucher applied!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Order.this, "Invalid voucher code", Toast.LENGTH_SHORT).show();
            }
        });
        /* ------------------------------------ */

        /* ---------- place‑order button ---------- */
        btnPlaceOrder.setOnClickListener(v -> {
            int id = paymentMethodGroup.getCheckedRadioButtonId();
            if (id != -1) {
                String method = ((RadioButton) findViewById(id)).getText().toString();

                Toast.makeText(Order.this,
                        "Order placed using " + method, Toast.LENGTH_LONG).show();

                // --- NEW: Push notification to seller ---
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String buyerName = "Someone"; // default fallback
                if (currentUser != null && currentUser.getDisplayName() != null) {
                    buyerName = currentUser.getDisplayName();
                }

                // Make sure your Product class has getSellerUid()
                String sellerUid = product.getSellerUid();

                if (sellerUid != null && !sellerUid.isEmpty()) {
                    DatabaseReference notifRef = FirebaseDatabase.getInstance()
                            .getReference("notifications")
                            .child(sellerUid);

                    Notification notification = new Notification(buyerName, product.getName());
                    notifRef.push().setValue(notification);
                } else {
                    Toast.makeText(Order.this, "Seller ID missing, notification not sent.", Toast.LENGTH_SHORT).show();
                }

                // TODO: continue to confirmation / API call here if needed

            } else {
                Toast.makeText(Order.this,
                        "Please select a payment method", Toast.LENGTH_SHORT).show();
            }
        });

    }
}