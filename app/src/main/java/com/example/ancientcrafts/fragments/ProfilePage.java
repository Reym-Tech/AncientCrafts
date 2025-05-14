package com.example.ancientcrafts.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ancientcrafts.CartProducts;
import com.example.ancientcrafts.MainActivity;
import com.example.ancientcrafts.R;
import com.example.ancientcrafts.AddProductActivity;
import com.example.ancientcrafts.FavoriteProducts;
import com.example.ancientcrafts.CartProducts;  // <-- Import your cart activity
import com.google.android.material.card.MaterialCardView;

public class ProfilePage extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_page, container, false);

        MaterialCardView sellBtn = view.findViewById(R.id.sellBtn);
        ImageView menuBtn = view.findViewById(R.id.menu_Btn);
        ImageView favBtn = view.findViewById(R.id.fav_Btn);
        ImageView cartBtn = view.findViewById(R.id.cart_Btn);  // <-- Reference to cart_Btn

        menuBtn.setOnClickListener(v -> showPopupMenu(v));
        sellBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddProductActivity.class);
            startActivity(intent);
        });

        favBtn.setOnClickListener(v -> openFavoriteProducts());

        // Handle cart button click
        cartBtn.setOnClickListener(v -> openCart());

        return view;
    }

    private void showPopupMenu(View view) {
        ContextThemeWrapper wrapper = new ContextThemeWrapper(requireContext(), R.style.PopupMenuStyle);
        PopupMenu popup = new PopupMenu(wrapper, view);
        popup.getMenuInflater().inflate(R.menu.profile_dropdown_menu, popup.getMenu());

        try {
            java.lang.reflect.Field[] fields = popup.getClass().getDeclaredFields();
            for (java.lang.reflect.Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    java.lang.reflect.Method setForceShowIcon = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceShowIcon.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_settings) {
                openSettings();
                return true;
            } else if (id == R.id.menu_help) {
                openHelpCenter();
                return true;
            } else if (id == R.id.menu_logout) {
                logoutUser();
                return true;
            }
            return false;
        });

        popup.show();
    }

    private void openFavoriteProducts() {
        Intent intent = new Intent(getActivity(), FavoriteProducts.class);
        startActivity(intent);
    }

    // New method to handle cart button action
    private void openCart() {
        Intent intent = new Intent(getActivity(), CartProducts.class);
        startActivity(intent);
    }

    private void openSettings() {
        // Add settings logic here
    }

    private void openHelpCenter() {
        // Add help center logic here
    }

    private void logoutUser() {
        android.content.SharedPreferences preferences = requireActivity()
                .getSharedPreferences("user_session", android.content.Context.MODE_PRIVATE);
        preferences.edit().clear().apply();

        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}
