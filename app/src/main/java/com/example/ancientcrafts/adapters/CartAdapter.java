package com.example.ancientcrafts.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ancientcrafts.models.CartItem;

import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartVH> {

    public interface OnItemClick {
        void onDelete(int position);
    }

    private final List<CartItem> items;
    private final OnItemClick listener;

    public CartAdapter(List<CartItem> items, OnItemClick listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull @Override
    public CartVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new CartVH(v);
    }

    @Override public void onBindViewHolder(@NonNull CartVH h, int pos) {
        CartItem item = items.get(pos);
        h.title.setText(item.getName());
        h.subtitle.setText(String.format(Locale.getDefault(), "₱%.2f", item.getPrice()));
        h.itemView.setOnClickListener(v -> listener.onDelete(pos));
    }

    @Override public int getItemCount() { return items.size(); }

    static class CartVH extends RecyclerView.ViewHolder {
        TextView title, subtitle;
        CartVH(View v) {
            super(v);
            title    = v.findViewById(android.R.id.text1);
            subtitle = v.findViewById(android.R.id.text2);
        }
    }
}
