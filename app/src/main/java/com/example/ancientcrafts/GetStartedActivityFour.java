package com.example.ancientcrafts;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class GetStartedActivityFour extends AppCompatActivity {

    private Handler handler = new Handler();
    private final Runnable navigateRunnable = this::navigateToMain;
    private boolean navigated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.get_started_activity_four);

        ImageButton nextButton = findViewById(R.id.next_button_4);
        nextButton.setOnClickListener(v -> {
            cancelAutoNavigate();
            navigateToMain();
        });

        // Automatically navigate after 3 seconds
        handler.postDelayed(navigateRunnable, 3000);
    }

    private void navigateToMain() {
        if (navigated) return; // prevent double call
        navigated = true;
        Intent intent = new Intent(GetStartedActivityFour.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void cancelAutoNavigate() {
        handler.removeCallbacks(navigateRunnable);
    }
}
