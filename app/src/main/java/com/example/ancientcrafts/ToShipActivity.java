package com.example.ancientcrafts;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ToShipActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.to_ship_activity);

        Button btnTrack = findViewById(R.id.btnTrack);
        btnTrack.setOnClickListener(v ->
                Toast.makeText(this, "Tracking shipment...", Toast.LENGTH_SHORT).show()
        );
    }
}

