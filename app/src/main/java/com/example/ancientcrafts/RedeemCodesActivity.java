package com.example.ancientcrafts;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RedeemCodesActivity extends AppCompatActivity {
    EditText redeemCodeInput;
    Button redeemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redeem_codes_activity);

        redeemCodeInput = findViewById(R.id.redeemCodeInput);
        redeemButton = findViewById(R.id.redeemButton);

        redeemButton.setOnClickListener(v -> {
            String code = redeemCodeInput.getText().toString().trim();
            if (!code.isEmpty()) {
                Toast.makeText(this, "Code \"" + code + "\" redeemed successfully!", Toast.LENGTH_SHORT).show();
                redeemCodeInput.setText("");

                // Navigate back to the main lobby (MainActivity)
                Intent intent = new Intent(RedeemCodesActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish(); // optional, to prevent going back to this screen
            } else {
                Toast.makeText(this, "Please enter a code first", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
