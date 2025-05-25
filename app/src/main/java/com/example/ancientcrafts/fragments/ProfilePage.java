package com.example.ancientcrafts.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ancientcrafts.AddProductActivity;
import com.example.ancientcrafts.CartProducts;
import com.example.ancientcrafts.ChatActivity;
import com.example.ancientcrafts.FavoriteProducts;
import com.example.ancientcrafts.HelpCenterActivity;
import com.example.ancientcrafts.MainActivity;
import com.example.ancientcrafts.MyCouponsActivity;
import com.example.ancientcrafts.RedeemCodesActivity;
import com.example.ancientcrafts.PurchaseHistoryActivity;
import com.example.ancientcrafts.R;
import com.example.ancientcrafts.SettingsActivity;
import com.example.ancientcrafts.ToPayActivity;
import com.example.ancientcrafts.ToRateActivity;
import com.example.ancientcrafts.ToReceiveActivity;
import com.example.ancientcrafts.ToShipActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class ProfilePage extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Bitmap selectedBitmap;
    private ImageView profileImage;
    private DatabaseReference userRef;
    private String userId;
    private TextView addressText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialCardView sellBtn = view.findViewById(R.id.sellBtn);
        ImageView menuBtn = view.findViewById(R.id.menu_Btn);
        ImageView favBtn = view.findViewById(R.id.fav_Btn);
        ImageView cartBtn = view.findViewById(R.id.cart_Btn);
        ImageView chatBtn = view.findViewById(R.id.message_Btn);
        ImageView toPayBtn = view.findViewById(R.id.toPay_Btn);
        ImageView toShipBtn = view.findViewById(R.id.toShip_Btn);
        ImageView toReceiveBtn = view.findViewById(R.id.toReceive_Btn);
        ImageView toRateBtn = view.findViewById(R.id.toRate_Btn);
        TextView purchaseHistoryBtn = view.findViewById(R.id.purchaseHAistory_Btn);
        RelativeLayout myCouponsBtn = view.findViewById(R.id.myCoupons_Btn);
        RelativeLayout redeemCodesBtn = view.findViewById(R.id.redeemCodes_Btn);

        TextView profileName = view.findViewById(R.id.profile_firstName);
        TextView profileEmail = view.findViewById(R.id.profile_email);
        TextView profilePNumber = view.findViewById(R.id.number_holder);
        profileImage = view.findViewById(R.id.profile_image);
        addressText = view.findViewById(R.id.profile_address);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
            userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

            // Load user info
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("first_name").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        String phone = snapshot.child("phone_number").getValue(String.class);
                        String imageUrl = snapshot.child("profile_image").getValue(String.class);
                        Boolean isSeller = snapshot.child("isSeller").getValue(Boolean.class);

                        profileName.setText(name != null ? name : "No Name");
                        profileEmail.setText(email != null ? email : "No Email");
                        profilePNumber.setText(phone != null ? phone : "No Phone");

                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            String imageFullUrl = "http://192.168.8.38/AncientCrafts_profileImg/" + imageUrl;
                            Glide.with(requireContext())
                                    .load(imageFullUrl)
                                    .circleCrop()
                                    .skipMemoryCache(true)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .into(profileImage);
                        }

                        // Check if user is a seller
                        TextView sellBtnText = view.findViewById(R.id.sellBtnText); // Add a TextView inside sellBtn in XML with this ID

                        if (isSeller != null && isSeller) {
                            sellBtn.setVisibility(View.VISIBLE);
                            sellBtnText.setText("Manage Products");
                            sellBtn.setOnClickListener(v -> {
                                startActivity(new Intent(getActivity(), AddProductActivity.class));
                            });
                        } else {
                            sellBtn.setVisibility(View.VISIBLE);
                            sellBtnText.setText("Start Selling");
                            sellBtn.setOnClickListener(v -> {
                                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                if (currentUser != null) {
                                    String uid = currentUser.getUid();
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(uid);

                                    ref.child("isSeller").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Boolean isSeller = snapshot.getValue(Boolean.class);
                                            if (Boolean.TRUE.equals(isSeller)) {
                                                // Already seller
                                                startActivity(new Intent(getActivity(), AddProductActivity.class));
                                            } else {
                                                // Launch fake ID verification
                                                startActivity(new Intent(getActivity(), com.example.ancientcrafts.FakeVerificationActivity.class));
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(getContext(), "Failed to check seller status", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Failed to load profile", Toast.LENGTH_SHORT).show();
                }
            });

            // Load address
            userRef.child("address").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String address = snapshot.getValue(String.class);
                        addressText.setText(address);
                    } else {
                        addressText.setText("Add Address");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        }

        profileImage.setOnClickListener(v -> chooseImage());
        view.findViewById(R.id.addressSection).setOnClickListener(v -> showAddressDialog());
        menuBtn.setOnClickListener(this::showCustomPopup);

        // Other click listeners
        favBtn.setOnClickListener(v -> startActivity(new Intent(getActivity(), FavoriteProducts.class)));
        cartBtn.setOnClickListener(v -> startActivity(new Intent(getActivity(), CartProducts.class)));
        chatBtn.setOnClickListener(v -> startActivity(new Intent(getActivity(), ChatActivity.class)));
        toPayBtn.setOnClickListener(v -> startActivity(new Intent(getActivity(), ToPayActivity.class)));
        toShipBtn.setOnClickListener(v -> startActivity(new Intent(getActivity(), ToShipActivity.class)));
        toReceiveBtn.setOnClickListener(v -> startActivity(new Intent(getActivity(), ToReceiveActivity.class)));
        toRateBtn.setOnClickListener(v -> startActivity(new Intent(getActivity(), ToRateActivity.class)));
        purchaseHistoryBtn.setOnClickListener(v -> startActivity(new Intent(getActivity(), PurchaseHistoryActivity.class)));
        myCouponsBtn.setOnClickListener(v -> startActivity(new Intent(getActivity(), MyCouponsActivity.class)));
        redeemCodesBtn.setOnClickListener(v -> startActivity(new Intent(getActivity(), RedeemCodesActivity.class)));
    }

    private void showAddressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter Delivery Address");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setHint("e.g., 123 Street, City, Country");
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newAddress = input.getText().toString().trim();
            if (!newAddress.isEmpty()) {
                userRef.child("address").setValue(newAddress).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        addressText.setText(newAddress);
                        Toast.makeText(getContext(), "Address updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to update", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == -1 && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                selectedBitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), filePath);
                profileImage.setImageBitmap(selectedBitmap);
                uploadProfileImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadProfileImage() {
        if (selectedBitmap == null) return;

        ProgressDialog dialog = ProgressDialog.show(getContext(), "Uploading", "Please wait...", true);
        String base64Image = bitmapToBase64(selectedBitmap);
        String imageName = userId + ".jpg";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                "http://192.168.8.38/AncientCrafts_profileImg/upload.php",
                response -> {
                    dialog.dismiss();
                    FirebaseDatabase.getInstance().getReference("users")
                            .child(userId)
                            .child("profile_image")
                            .setValue(imageName);
                    Toast.makeText(getContext(), "Profile image updated", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    dialog.dismiss();
                    Toast.makeText(getContext(), "Upload failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected HashMap<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("image", base64Image);
                params.put("name", imageName);
                return params;
            }
        };
        Volley.newRequestQueue(requireContext()).add(request);
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] bytes = baos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private void showCustomPopup(View anchor) {
        View popupView = LayoutInflater.from(getContext()).inflate(R.layout.custom_popup_menu, null);
        PopupWindow popupWindow = new PopupWindow(popupView,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                true);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(anchor);

        popupView.findViewById(R.id.menu_settings).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            popupWindow.dismiss();
        });

        popupView.findViewById(R.id.menu_help).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), HelpCenterActivity.class));
            popupWindow.dismiss();
        });

        popupView.findViewById(R.id.menu_logout).setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getActivity(), MainActivity.class));
            requireActivity().finish();
            popupWindow.dismiss();
        });
    }
}