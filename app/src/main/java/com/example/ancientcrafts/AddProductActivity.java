package com.example.ancientcrafts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ancientcrafts.models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class AddProductActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private TextView sellerNameTextView, sellerEmailTextView, sellerNumberTextView, sellerAddressTextView;
    private EditText editName, editPrice;
    private ImageView selectedImageView;
    private Button btnChooseImage, btnSubmit, btnViewOrders, btnViewProducts, btnAnalytics;
    private Bitmap selectedBitmap;

    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        editName = findViewById(R.id.edit_name);
        editPrice = findViewById(R.id.edit_price);
        selectedImageView = findViewById(R.id.image_preview);
        btnChooseImage = findViewById(R.id.btn_choose_image);
        btnSubmit = findViewById(R.id.btn_submit);
        btnViewOrders = findViewById(R.id.btn_view_orders);
        btnViewProducts = findViewById(R.id.btn_view_products);
        btnAnalytics = findViewById(R.id.btn_analytics);
        sellerNameTextView = findViewById(R.id.seller_name);
        sellerEmailTextView = findViewById(R.id.seller_email);
        sellerNumberTextView = findViewById(R.id.seller_number);
        sellerAddressTextView = findViewById(R.id.seller_address);


        productsRef = FirebaseDatabase.getInstance().getReference("products");

        btnChooseImage.setOnClickListener(v -> chooseImage());

        btnSubmit.setOnClickListener(v -> {
            if (selectedBitmap == null) {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            } else {
                uploadProduct();
            }
        });

        btnViewOrders.setOnClickListener(v -> {
            Intent intent = new Intent(AddProductActivity.this, OrdersItem.class);
            startActivity(intent);
        });

        btnViewProducts.setOnClickListener(v -> {
            Intent intent = new Intent(AddProductActivity.this, ProductsItem.class);
            startActivity(intent);
        });

        btnAnalytics.setOnClickListener(v -> {
            Intent intent = new Intent(AddProductActivity.this, AnalyticsActivity.class);
            startActivity(intent);
        });
        // Fetch and display seller name and email
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
            userRef.get().addOnSuccessListener(snapshot -> {
                if (snapshot.exists()) {
                    String name = snapshot.child("first_name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String number = snapshot.child("phone_number").getValue(String.class);
                    String address = snapshot.child("address").getValue(String.class);

                    sellerNameTextView.setText(" " + name);
                    sellerEmailTextView.setText("Email: " + email);
                    sellerNumberTextView.setText("Phone: " + number);
                    sellerAddressTextView.setText("Address: " + address);
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(AddProductActivity.this, "Failed to load seller info", Toast.LENGTH_SHORT).show();
            });
        }

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Product Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                selectedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                selectedImageView.setImageBitmap(selectedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadProduct() {
        String name = editName.getText().toString().trim();
        String priceStr = editPrice.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }
        String sellerUid = user.getUid();  // Get current user's UID

        ProgressDialog dialog = ProgressDialog.show(this, "Uploading", "Please wait...", true);

        String base64Image = bitmapToBase64(selectedBitmap);
        String imageName = "img_" + System.currentTimeMillis() + ".jpg";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                "http://192.168.8.38/AncientCrafts_productImg/upload.php",
                response -> {
                    dialog.dismiss();

                    double price = Double.parseDouble(priceStr);
                    int id = (int) (System.currentTimeMillis() / 1000);

                    // Use full constructor with all params
                    Product product = new Product(
                            id,
                            name,
                            imageName,
                            price,
                            price * 1.5,       // originalPrice
                            0,                 // soldCount
                            4.0f,              // rating
                            0,                 // reviewCount
                            "Premium quality product", // description
                            "3-5 days",               // deliveryEstimate
                            sellerUid          // seller UID
                    );

                    productsRef.child(String.valueOf(id)).setValue(product)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Product added!", Toast.LENGTH_SHORT).show();
                                finish();
                            })
                            .addOnFailureListener(e -> Toast.makeText(this, "Upload failed", Toast.LENGTH_SHORT).show());
                },
                error -> {
                    dialog.dismiss();
                    Toast.makeText(this, "Upload error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
        ) {
            @Override
            protected java.util.Map<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("image", base64Image);
                params.put("name", imageName);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] imageBytes = stream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}
