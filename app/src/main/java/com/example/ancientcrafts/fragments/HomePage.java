package com.example.ancientcrafts.fragments;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ancientcrafts.R;
import com.example.ancientcrafts.adapters.ProductAdapter;
import com.example.ancientcrafts.models.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class HomePage extends Fragment {
    private RecyclerView productsRecyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();
    private ImageView filterBtn;

    private PopupWindow filterPopupWindow;

    private final List<String> selectedFilters = new ArrayList<>();
    private CompoundButton.OnCheckedChangeListener allListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmenthomepage, container, false);

        // Initialize views
        productsRecyclerView = view.findViewById(R.id.products_grid);
        filterBtn = view.findViewById(R.id.filter_Btn);

        setupRecyclerView();
        loadProductsFromFirebase();

        filterBtn.setOnClickListener(v -> showFilterMenu(v));

        return view;
    }

    private void setupRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        productsRecyclerView.setLayoutManager(layoutManager);
        productAdapter = new ProductAdapter(productList, getContext(), R.drawable.placeholder_image);
        productsRecyclerView.setAdapter(productAdapter);
    }

    private void loadProductsFromFirebase() {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("products");

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    if (product != null) {
                        productList.add(product);
                    }
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load products: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showFilterMenu(View anchor) {
        View popupView = getLayoutInflater().inflate(R.layout.popup_filter_checklist, null);

        CheckBox checkAll = popupView.findViewById(R.id.check_all);
        CheckBox checkNewest = popupView.findViewById(R.id.check_newest);
        CheckBox checkLowHigh = popupView.findViewById(R.id.check_price_low_high);
        CheckBox checkHighLow = popupView.findViewById(R.id.check_price_high_low);
        CheckBox checkTopRated = popupView.findViewById(R.id.check_top_rated);
        Button btnApply = popupView.findViewById(R.id.btn_apply_filters);

        // Set initial checkbox states based on selectedFilters
        checkAll.setChecked(selectedFilters.contains("All"));
        checkNewest.setChecked(selectedFilters.contains("Newest"));
        checkLowHigh.setChecked(selectedFilters.contains("Price: Low to High"));
        checkHighLow.setChecked(selectedFilters.contains("Price: High to Low"));
        checkTopRated.setChecked(selectedFilters.contains("Top Rated"));

        // Helper: Update "All" checkbox state based on individual checkboxes
        Runnable updateAllCheckbox = () -> {
            boolean allChecked = checkNewest.isChecked() && checkLowHigh.isChecked()
                    && checkHighLow.isChecked() && checkTopRated.isChecked();
            checkAll.setOnCheckedChangeListener(null); // temporarily remove listener to avoid loop
            checkAll.setChecked(allChecked);
            checkAll.setOnCheckedChangeListener(allListener);
        };

        // Listener for "All" checkbox to toggle all others
        CompoundButton.OnCheckedChangeListener allListener = (buttonView, isChecked) -> {
            // When "All" is checked or unchecked, toggle all others accordingly
            checkNewest.setChecked(isChecked);
            checkLowHigh.setChecked(isChecked);
            checkHighLow.setChecked(isChecked);
            checkTopRated.setChecked(isChecked);
        };

        checkAll.setOnCheckedChangeListener(allListener);

        // Listeners for individual checkboxes to update "All"
        CompoundButton.OnCheckedChangeListener individualListener = (buttonView, isChecked) -> {
            updateAllCheckbox.run();
        };

        checkNewest.setOnCheckedChangeListener(individualListener);
        checkLowHigh.setOnCheckedChangeListener(individualListener);
        checkHighLow.setOnCheckedChangeListener(individualListener);
        checkTopRated.setOnCheckedChangeListener(individualListener);

        // Create PopupWindow
        filterPopupWindow = new PopupWindow(
                popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true
        );
        filterPopupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        filterPopupWindow.showAsDropDown(anchor);

        btnApply.setOnClickListener(v -> {
            selectedFilters.clear();
            if (checkAll.isChecked()) selectedFilters.add("All");
            if (checkNewest.isChecked()) selectedFilters.add("Newest");
            if (checkLowHigh.isChecked()) selectedFilters.add("Price: Low to High");
            if (checkHighLow.isChecked()) selectedFilters.add("Price: High to Low");
            if (checkTopRated.isChecked()) selectedFilters.add("Top Rated");

            Toast.makeText(getContext(), "Filters applied: " + selectedFilters, Toast.LENGTH_SHORT).show();

            // TODO: apply your filtering logic here based on selectedFilters

            filterPopupWindow.dismiss();
        });
    }


}
