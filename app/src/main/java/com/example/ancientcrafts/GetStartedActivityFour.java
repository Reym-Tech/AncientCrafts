package com.example.ancientcrafts;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class GetStartedActivityFour extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.get_started_activity_four);

        ImageButton nextButton = findViewById(R.id.next_button_4);
        nextButton.setOnClickListener(v -> {
            navigateToMain();
        });

        // Optionally navigate automatically after 3 seconds
        new Handler().postDelayed(this::navigateToMain, 3000);
    }

    private void navigateToMain() {
        Intent intent = new Intent(GetStartedActivityFour.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}