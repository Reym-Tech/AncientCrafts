package com.example.ancientcrafts.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ancientcrafts.R;
import com.example.ancientcrafts.models.OrderModel;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<OrderModel> orderList;
    private OnOrderClickListener listener;

    // Interface for handling clicks
    public interface OnOrderClickListener {
        void onOrderClick(OrderModel order);
    }

    // Updated constructor
    public OrderAdapter(Context context, List<OrderModel> orderList, OnOrderClickListener listener) {
        this.context = context;
        this.orderList = orderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderModel order = orderList.get(position);

        holder.productName.setText(order.getProductName());
        holder.quantity.setText("Qty: " + order.getQuantity());
        holder.totalPrice.setText("₱" + formatPrice(order.getTotalPrice()));

        Glide.with(context)
                .load(order.getProductImageUrl())
                .placeholder(R.drawable.error_img)
                .into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, quantity, totalPrice;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.imgOrderProduct);
            productName = itemView.findViewById(R.id.txtOrderProductName);
            quantity = itemView.findViewById(R.id.txtOrderQuantity);
            totalPrice = itemView.findViewById(R.id.txtOrderTotalPrice);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onOrderClick(orderList.get(position));
                }
            });
        }
    }

    private String formatPrice(double price) {
        return NumberFormat.getNumberInstance(Locale.US).format(price);
    }
}
