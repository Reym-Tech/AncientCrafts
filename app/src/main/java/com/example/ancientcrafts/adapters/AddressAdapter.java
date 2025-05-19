package com.example.ancientcrafts.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ancientcrafts.R;
import com.example.ancientcrafts.models.Address;

public class AddressAdapter extends ListAdapter<Address, AddressAdapter.AddressViewHolder> {

    public interface OnAddressClickListener {
        void onAddressClick(Address address);
    }

    private final OnAddressClickListener listener;

    public AddressAdapter(OnAddressClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<Address> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Address>() {
                @Override
                public boolean areItemsTheSame(@NonNull Address oldItem, @NonNull Address newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Address oldItem, @NonNull Address newItem) {
                    return oldItem.getFullAddress().equals(newItem.getFullAddress());
                }
            };

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_activity, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        Address address = getItem(position);
        holder.bind(address);
    }

    class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView textAddress;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            textAddress = itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && listener != null) {
                    listener.onAddressClick(getItem(pos));
                }
            });
        }

        void bind(Address address) {
            textAddress.setText(address.getFullAddress());
        }
    }
}
