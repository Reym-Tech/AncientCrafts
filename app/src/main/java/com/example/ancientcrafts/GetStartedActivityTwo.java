package com.example.ancientcrafts;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class GetStartedActivityTwo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.get_started_activity_two);

        ImageButton nextButton = findViewById(R.id.next_button_2);
        nextButton.setOnClickListener(v -> {
            Intent intent = new Intent(GetStartedActivityTwo.this, GetStartedActivityThree.class);
            startActivity(intent);// Prevent returning to Welcome screen on back press
        });
    }
}
