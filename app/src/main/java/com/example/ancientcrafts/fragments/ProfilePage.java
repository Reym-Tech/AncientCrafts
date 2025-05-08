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
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.ancientcrafts.MainActivity;
import com.example.ancientcrafts.R;

public class ProfilePage extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_page, container, false);

        ImageView menuBtn = view.findViewById(R.id.menu_Btn);
        menuBtn.setOnClickListener(v -> showPopupMenu(v));

        return view;
    }

    private void showPopupMenu(View view) {
        // Apply our custom style to the popup menu
        ContextThemeWrapper wrapper = new ContextThemeWrapper(requireContext(), R.style.PopupMenuStyle);
        PopupMenu popup = new PopupMenu(wrapper, view);
        popup.getMenuInflater().inflate(R.menu.profile_dropdown_menu, popup.getMenu());

        // Force show icons (using reflection as a workaround)
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

        // Set item click listener
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
        // Implement your settings opening logic here
        // Example: startActivity(new Intent(getActivity(), SettingsActivity.class));
    }

    private void openHelpCenter() {
        // Implement your help center opening logic here
        // Example: startActivity(new Intent(getActivity(), HelpActivity.class));
    }

    private void logoutUser() {
        // Clear any session data if needed
        android.content.SharedPreferences preferences = requireActivity()
                .getSharedPreferences("user_session", android.content.Context.MODE_PRIVATE);
        preferences.edit().clear().apply();

        // Redirect to login and clear back stack
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}