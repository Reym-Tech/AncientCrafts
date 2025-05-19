package com.example.ancientcrafts;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ToPayActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.to_pay_activity);

        Button btnPayNow = findViewById(R.id.btnProceedToPay);
        btnPayNow.setOnClickListener(v -> {
            // Handle pay logic here
            Toast.makeText(this, "Proceeding to payment...", Toast.LENGTH_SHORT).show();
        });
    }
}

