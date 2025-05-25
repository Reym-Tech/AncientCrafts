package com.example.ancientcrafts;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ancientcrafts.adapters.OrderAdapter;
import com.example.ancientcrafts.models.OrderModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ToPayActivity extends AppCompatActivity implements OrderAdapter.OnOrderClickListener {

    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private List<OrderModel> orderList;
    private TextView totalPriceTextView;

    private DatabaseReference ordersRef;
    private String currentUserUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.to_pay_activity);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        totalPriceTextView = findViewById(R.id.totalPrice);

        Button btnPayNow = findViewById(R.id.btnProceedToPay);
        btnPayNow.setOnClickListener(v -> {
            Toast.makeText(this, "Proceeding to payment...", Toast.LENGTH_SHORT).show();
        });

        recyclerView = findViewById(R.id.recyclerViewToPay);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderList = new ArrayList<>();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        currentUserUid = currentUser.getUid();
        ordersRef = FirebaseDatabase.getInstance().getReference("orders");

        adapter = new OrderAdapter(this, orderList, this); // Pass the listener
        recyclerView.setAdapter(adapter);

        loadToPayOrders();
    }

    private void loadToPayOrders() {
        ordersRef.orderByChild("buyerUid").equalTo(currentUserUid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        orderList.clear();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            String status = snap.child("orderStatus").getValue(String.class);
                            if ("toPay".equals(status)) {
                                OrderModel order = snap.getValue(OrderModel.class);
                                if (order != null) {
                                    order.setOrderId(snap.getKey()); // save Firebase key
                                    orderList.add(order);
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                        updateTotalPrice();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ToPayActivity.this, "Failed to load orders", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateTotalPrice() {
        double total = 0;
        for (OrderModel order : orderList) {
            total += order.getTotalPrice();
        }
        totalPriceTextView.setText("₱" + String.format("%.2f", total));
    }

    @Override
    public void onOrderClick(OrderModel order) {
        new AlertDialog.Builder(this)
                .setTitle("Cancel Order")
                .setMessage("Are you sure you want to cancel this order?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    cancelOrder(order);
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void cancelOrder(OrderModel order) {
        if (order.getOrderId() == null) {
            Toast.makeText(this, "Order ID missing, cannot cancel", Toast.LENGTH_SHORT).show();
            return;
        }

        ordersRef.child(order.getOrderId()).removeValue()
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Order cancelled", Toast.LENGTH_SHORT).show();
                    orderList.remove(order);
                    adapter.notifyDataSetChanged();
                    updateTotalPrice();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to cancel order", Toast.LENGTH_SHORT).show();
                });
    }
}
