package com.example.ancientcrafts;

import com.example.ancientcrafts.models.Product;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private final List<Product> cartList;

    private CartManager() {
        cartList = new ArrayList<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addToCart(Product product) {
        cartList.add(product);
    }

    public List<Product> getCartItems() {
        return cartList;
    }

    public void clearCart() {
        cartList.clear();
    }
}
