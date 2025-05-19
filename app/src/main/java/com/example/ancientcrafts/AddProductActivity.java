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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ancientcrafts.models.Product;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class AddProductActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editName, editPrice;
    private ImageView selectedImageView;
    private Button btnChooseImage, btnSubmit;
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

        productsRef = FirebaseDatabase.getInstance().getReference("products");

        btnChooseImage.setOnClickListener(v -> chooseImage());

        btnSubmit.setOnClickListener(v -> {
            if (selectedBitmap == null) {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            } else {
                uploadProduct();
            }
        });
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

        ProgressDialog dialog = ProgressDialog.show(this, "Uploading", "Please wait...", true);

        String base64Image = bitmapToBase64(selectedBitmap);
        String imageName = "img_" + System.currentTimeMillis() + ".jpg";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                "http://10.10.10.147/AncientCrafts_productImg/upload.php" ,
                response -> {
                    dialog.dismiss();
                    String imageUrl = imageName;

                    double price = Double.parseDouble(priceStr);
                    int id = (int) (System.currentTimeMillis() / 1000);
                    Product product = new Product(id, name, imageUrl, price);

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
