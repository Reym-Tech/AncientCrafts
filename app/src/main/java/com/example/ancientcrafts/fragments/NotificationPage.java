package com.example.ancientcrafts.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ancientcrafts.R;
import com.example.ancientcrafts.adapters.NotificationAdapter;
import com.example.ancientcrafts.models.Notification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NotificationPage extends Fragment {

    private NotificationAdapter adapter;
    private DatabaseReference notifRef;
    private ChildEventListener childListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notification_page, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.notificationRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotificationAdapter();
        recyclerView.setAdapter(adapter);

        String sellerUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        notifRef = FirebaseDatabase.getInstance()
                .getReference("notifications")
                .child(sellerUid);

        childListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snap, @Nullable String prevKey) {
                Notification n = snap.getValue(Notification.class);
                if (n != null) adapter.addFirst(n);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };

        notifRef.limitToLast(20).addChildEventListener(childListener);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (notifRef != null && childListener != null) {
            notifRef.removeEventListener(childListener);
        }
    }
}