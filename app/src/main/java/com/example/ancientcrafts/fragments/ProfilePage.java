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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ancientcrafts.AddProductActivity;
import com.example.ancientcrafts.CartProducts;
import com.example.ancientcrafts.ChatActivity;
import com.example.ancientcrafts.FavoriteProducts;
import com.example.ancientcrafts.MainActivity;
import com.example.ancientcrafts.R;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class ProfilePage extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_page, container, false);

        // UI Elements
        MaterialCardView sellBtn = view.findViewById(R.id.sellBtn);
        ImageView menuBtn = view.findViewById(R.id.menu_Btn);
        ImageView favBtn = view.findViewById(R.id.fav_Btn);
        ImageView cartBtn = view.findViewById(R.id.cart_Btn);
        ImageView chatBtn = view.findViewById(R.id.message_Btn);

        TextView profileName = view.findViewById(R.id.profile_firstName);
        TextView profileEmail = view.findViewById(R.id.profile_email);
        TextView profilePNumber = view.findViewById(R.id.number_holder);

        // Load current user data
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();

            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("first_name").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        String phone_number = snapshot.child("phone_number").getValue(String.class);

                        profileName.setText(name != null ? name : "No Name");
                        profileEmail.setText(email != null ? email : "No Email");
                        profilePNumber.setText(phone_number != null ? phone_number : "No Phone Number");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Failed to load user data", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Buttons
        menuBtn.setOnClickListener(this::showPopupMenu);
        sellBtn.setOnClickListener(v -> startActivity(new Intent(getActivity(), AddProductActivity.class)));
        favBtn.setOnClickListener(v -> startActivity(new Intent(getActivity(), FavoriteProducts.class)));
        cartBtn.setOnClickListener(v -> startActivity(new Intent(getActivity(), CartProducts.class)));
        chatBtn.setOnClickListener(v -> startActivity(new Intent(getActivity(), ChatActivity.class)));

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

    private void openSettings() {
        // Add settings logic here
    }

    private void openHelpCenter() {
        // Add help center logic here
    }

    private void logoutUser() {
        requireActivity().getSharedPreferences("user_session", android.content.Context.MODE_PRIVATE)
                .edit().clear().apply();

        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}
