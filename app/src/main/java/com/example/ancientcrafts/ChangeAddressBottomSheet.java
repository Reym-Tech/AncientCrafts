package com.example.ancientcrafts;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ancientcrafts.adapters.AddressAdapter;
import com.example.ancientcrafts.models.Address;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class ChangeAddressBottomSheet extends BottomSheetDialogFragment {

    public interface Listener {
        void onAddressSelected(Address address);
    }

    private Listener listener;
    private AddressAdapter adapter;

    private List<Address> addressList = new ArrayList<>(); // simple in-memory list

    private EditText inputAddress;
    private Button btnAddEdit, btnSave;

    public void setListener(Listener l) {
        listener = l;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bs_change_address, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView rv = view.findViewById(R.id.recyclerAddresses);
        btnAddEdit = view.findViewById(R.id.btnAddEdit);

        // We'll add input fields dynamically
        inputAddress = view.findViewById(R.id.inputAddress); // You must add this EditText in bs_change_address.xml
        btnSave = view.findViewById(R.id.btnSave);           // You must add this Button in bs_change_address.xml

        adapter = new AddressAdapter(a -> {
            if (listener != null) listener.onAddressSelected(a);
            dismiss();
        });
        rv.setAdapter(adapter);

        btnAddEdit.setOnClickListener(v -> showInputForm());

        btnSave.setOnClickListener(v -> saveAddress());

        refresh();

        // Initially hide input fields
        inputAddress.setVisibility(View.GONE);
        btnSave.setVisibility(View.GONE);
    }

    private void showInputForm() {
        inputAddress.setVisibility(View.VISIBLE);
        btnSave.setVisibility(View.VISIBLE);
        btnAddEdit.setVisibility(View.GONE);
    }

    private void saveAddress() {
        String newAddressStr = inputAddress.getText().toString().trim();
        if (TextUtils.isEmpty(newAddressStr)) {
            Toast.makeText(getContext(), "Address cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        Address newAddress = new Address(newAddressStr);
        addressList.add(newAddress);
        refresh();

        // Reset input
        inputAddress.setText("");
        inputAddress.setVisibility(View.GONE);
        btnSave.setVisibility(View.GONE);
        btnAddEdit.setVisibility(View.VISIBLE);
    }

    private void refresh() {
        adapter.submitList(new ArrayList<>(addressList)); // defensive copy
    }
}
