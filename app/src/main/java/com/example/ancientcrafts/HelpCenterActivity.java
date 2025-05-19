package com.example.ancientcrafts;

import android.os.Bundle;
import android.widget.ExpandableListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HelpCenterActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    List<String> listQuestions;
    HashMap<String, List<String>> listAnswers;
    FAQExpandableListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_center_activity);

        expandableListView = findViewById(R.id.faqExpandableListView);
        listQuestions = new ArrayList<>();
        listAnswers = new HashMap<>();

        // Sample data
        listQuestions.add("How to track my order?");
        listQuestions.add("How to apply a coupon?");
        listQuestions.add("How to contact support?");

        List<String> answer1 = new ArrayList<>();
        answer1.add("Go to 'To Receive' section and track your orders there.");

        List<String> answer2 = new ArrayList<>();
        answer2.add("You can apply coupons at checkout in the cart.");

        List<String> answer3 = new ArrayList<>();
        answer3.add("Use the chat feature or email us at support@ancientcrafts.com.");

        listAnswers.put(listQuestions.get(0), answer1);
        listAnswers.put(listQuestions.get(1), answer2);
        listAnswers.put(listQuestions.get(2), answer3);

        adapter = new FAQExpandableListAdapter(this, listQuestions, listAnswers);
        expandableListView.setAdapter(adapter);
    }
}
