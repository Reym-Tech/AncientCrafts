package com.example.ancientcrafts;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

public class FakeVerificationActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView idPreviewImage;
    private Uri imageUri;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_verification);

        idPreviewImage = findViewById(R.id.idPreviewImage);
        Button uploadIdButton = findViewById(R.id.uploadIdButton);
        Button submitButton = findViewById(R.id.submitButton);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);

        uploadIdButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Terms & Permissions")
                    .setMessage("By uploading a valid ID, you confirm that:\n\n" +
                            "• You are the legal owner of the account.\n" +
                            "• The uploaded image is a valid and clear photo of your government-issued ID.\n" +
                            "• You agree to our marketplace terms and conditions.\n\n" +
                            "This information will be used for verification purposes only.")
                    .setPositiveButton("Agree & Continue", (dialog, which) -> {
                        // Automatically check the box
                        CheckBox agreeCheckbox = findViewById(R.id.agreeCheckbox);
                        agreeCheckbox.setChecked(true);

                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, PICK_IMAGE_REQUEST);
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });


        submitButton.setOnClickListener(v -> {
            CheckBox agreeCheckbox = findViewById(R.id.agreeCheckbox);
            if (!agreeCheckbox.isChecked()) {
                Toast.makeText(this, "You must agree to the terms to continue.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (imageUri != null) {
                Toast.makeText(this, "Verifying ID...", Toast.LENGTH_SHORT).show();
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    userRef.child("isSeller").setValue(true).addOnSuccessListener(unused -> {
                        Toast.makeText(this, "You're now a verified seller!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(this, AddProductActivity.class));
                        finish();
                    });
                }, 2000);
            } else {
                Toast.makeText(this, "Please select an ID image first.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            idPreviewImage.setImageURI(imageUri);
        }
    }
}
