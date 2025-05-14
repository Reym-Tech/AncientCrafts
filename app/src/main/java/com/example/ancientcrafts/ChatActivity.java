package com.example.ancientcrafts;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ancientcrafts.adapters.MessageAdapter;
import com.example.ancientcrafts.models.ChatMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerMessages;
    private EditText messageInput;
    private ImageButton sendBtn;
    private DatabaseReference chatRef;
    private FirebaseAuth auth;
    private String receiverId, senderId;

    private List<ChatMessage> messageList;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        recyclerMessages = findViewById(R.id.recyclerMessages);
        messageInput = findViewById(R.id.messageInput);
        sendBtn = findViewById(R.id.sendBtn);

        auth = FirebaseAuth.getInstance();
        senderId = auth.getCurrentUser().getUid();
        receiverId = getIntent().getStringExtra("receiverId");

        chatRef = FirebaseDatabase.getInstance().getReference("chats");

        // Setup RecyclerView
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList, senderId);
        recyclerMessages.setLayoutManager(new LinearLayoutManager(this));
        recyclerMessages.setAdapter(messageAdapter);

        // Load messages realtime
        loadMessages();

        sendBtn.setOnClickListener(v -> {
            String text = messageInput.getText().toString().trim();
            if (!text.isEmpty()) {
                ChatMessage msg = new ChatMessage(senderId, receiverId, text, System.currentTimeMillis());
                chatRef.push().setValue(msg);
                messageInput.setText("");
            }
        });
    }

    private void loadMessages() {
        chatRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                ChatMessage msg = snapshot.getValue(ChatMessage.class);
                if (msg == null) return;

                // Show only messages between these two users
                boolean isRelevant = (msg.getSenderId().equals(senderId) && msg.getReceiverId().equals(receiverId))
                        || (msg.getSenderId().equals(receiverId) && msg.getReceiverId().equals(senderId));
                if (isRelevant) {
                    messageList.add(msg);
                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                    recyclerMessages.scrollToPosition(messageList.size() - 1);
                }
            }

            @Override public void onChildChanged(DataSnapshot snapshot, String previousChildName) {}
            @Override public void onChildRemoved(DataSnapshot snapshot) {}
            @Override public void onChildMoved(DataSnapshot snapshot, String previousChildName) {}
            @Override public void onCancelled(DatabaseError error) {}
        });
    }
}

