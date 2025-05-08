package com.example.ancientcrafts.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ancientcrafts.ProductAdapter;
import com.example.ancientcrafts.ProductView;
import com.example.ancientcrafts.R;
import com.example.ancientcrafts.models.Product;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HomePage extends Fragment {
    private GridLayout productsGrid;
    private List<Product> productList = new ArrayList<>();

    private RecyclerView productsGrid1;
    private ProductAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmenthomepage, container, false);

        productsGrid = view.findViewById(R.id.products_grid);

        // Load products from database
        new LoadProductsTask().execute();

        return view;

    }

    private class LoadProductsTask extends AsyncTask<Void, Void, List<Product>> {
        @Override
        protected List<Product> doInBackground(Void... voids) {
            List<Product> products = new ArrayList<>();

            try {
                URL url = new URL("http://192.168.137.228/db_test_php/get_products.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000); // 5 seconds timeout
                conn.setReadTimeout(5000);

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));

                    StringBuilder jsonResults = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        jsonResults.append(line);
                    }
                    reader.close();

                    // Parse JSON response
                    JSONArray jsonArray = new JSONArray(jsonResults.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        products.add(new Product(
                                obj.getInt("id"),
                                obj.getString("name"),
                                obj.getString("image_name"),
                                obj.getDouble("price")
                        ));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return products;
        }

        @Override
        protected void onPostExecute(List<Product> products) {
            if (getActivity() != null && isAdded()) { // Check if fragment is still attached
                productList = products;
                if (productList.isEmpty()) {
                    // Show empty state or error message
                } else {
                    displayProducts();
                }
            }
        }
    }

    private void displayProducts() {
        productsGrid.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(getContext());

        for (Product product : productList) {
            // Inflate product card layout
            View productCard = inflater.inflate(R.layout.product_card, productsGrid, false);

            // Set product data
            ImageView productImage = productCard.findViewById(R.id.product_image);
            TextView productName = productCard.findViewById(R.id.product_name);
            TextView productPrice = productCard.findViewById(R.id.product_price);

            // Get resource ID for image
            int resId = getResources().getIdentifier(
                    product.getImageName(),
                    "drawable",
                    requireContext().getPackageName()
            );

            productImage.setImageResource(resId);
            productName.setText(product.getName());
            productPrice.setText(String.format("₱%.2f", product.getPrice()));

            // Set click listener
            productCard.setOnClickListener(v -> {
                // Handle product click
                Intent intent = new Intent(getActivity(), ProductView.class);
                intent.putExtra("product_id", product.getId());
                startActivity(intent);
            });

            // Add to grid
            productsGrid.addView(productCard);
        }

    }

}