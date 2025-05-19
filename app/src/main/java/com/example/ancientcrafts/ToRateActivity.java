package com.example.ancientcrafts;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ancientcrafts.fragments.HomePage;

public class ToRateActivity extends AppCompatActivity {

    private Button submitRatingsBtn;
    private RatingBar ratingBar;
    private EditText commentInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.to_rate_activity);

        ratingBar = findViewById(R.id.ratingBar);
        commentInput = findViewById(R.id.commentInput);
        submitRatingsBtn = findViewById(R.id.submitRatingsBtn);

        submitRatingsBtn.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            String comment = commentInput.getText().toString();

            // TODO: Upload or save the rating and comment here

            Toast.makeText(this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show();

            // Navigate back to main menu or lobby (replace MainMenuActivity with your actual main activity)
            Intent intent = new Intent(ToRateActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // close ToRateActivity
        });
    }
}
