package com.example.ancientcrafts;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ancientcrafts.models.Product;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddProductActivity extends AppCompatActivity {
    private EditText editName, editPrice, editImageUrl;
    private Button btnSubmit;
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        editName = findViewById(R.id.edit_name);
        editPrice = findViewById(R.id.edit_price);
        editImageUrl = findViewById(R.id.edit_image_url);
        btnSubmit = findViewById(R.id.btn_submit);

        productsRef = FirebaseDatabase.getInstance().getReference("products");

        btnSubmit.setOnClickListener(v -> addProduct());
    }

    private void addProduct() {
        String name = editName.getText().toString();
        String priceStr = editPrice.getText().toString();
        String imageUrl = editImageUrl.getText().toString();

        if (name.isEmpty() || priceStr.isEmpty() || imageUrl.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        int id = (int) (System.currentTimeMillis() / 1000); // Unique ID

        Product product = new Product(id, name, imageUrl, price);
        productsRef.child(String.valueOf(id)).setValue(product)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Product added!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to add product", Toast.LENGTH_SHORT).show());
    }
}
