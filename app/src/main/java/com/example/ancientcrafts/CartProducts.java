package com.example.ancientcrafts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.ancientcrafts.models.Product;

import java.util.List;
import java.util.Locale;

public class CartProducts extends AppCompatActivity {

    private LinearLayout cartListLayout;
    private TextView totalTxt;
    private List<Product> cartItems;   // reference to the live list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_products_activity);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        cartListLayout = findViewById(R.id.cart_product_list_layout);
        totalTxt       = findViewById(R.id.cartTotalPrice);

        cartItems = CartManager.getInstance().getCartItems();   // the same list used elsewhere
        populateList();
        updateTotal();
    }

    /** Inflate a view for every Product and attach click‑to‑delete logic */
    private void populateList() {
        LayoutInflater inflater = LayoutInflater.from(this);
        cartListLayout.removeAllViews();        // in case we refresh

        for (int i = 0; i < cartItems.size(); i++) {
            int index = i;                      // final for inner class
            Product product = cartItems.get(i);

            View itemView = inflater.inflate(R.layout.cart_item, cartListLayout, false);

            ImageView imageView = itemView.findViewById(R.id.cart_item_image);
            TextView  nameView  = itemView.findViewById(R.id.cart_item_name);
            TextView  priceView = itemView.findViewById(R.id.cart_item_price);

            nameView .setText(product.getName());
            priceView.setText(String.format(Locale.getDefault(),"₱%.2f", product.getPrice()));

            Glide.with(this)
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_img)
                    .into(imageView);

            // ---- delete on click ----
            itemView.setOnClickListener(v -> showDeleteDialog(index));

            cartListLayout.addView(itemView);
        }
    }

    /** Show a confirm dialog, then delete */
    private void showDeleteDialog(int position) {
        Product p = cartItems.get(position);

        new AlertDialog.Builder(this)
                .setTitle("Remove item?")
                .setMessage("Delete \"" + p.getName() + "\" from your cart?")
                .setPositiveButton("Delete", (d, w) -> {
                    cartItems.remove(position);          // remove from data source
                    cartListLayout.removeViewAt(position); // remove the view
                    updateTotal();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    /** Re‑sum the cart and render */
    private void updateTotal() {
        double sum = 0;
        for (Product p : cartItems) sum += p.getPrice();
        totalTxt.setText(String.format(Locale.getDefault(), "₱%.2f", sum));
    }
}
