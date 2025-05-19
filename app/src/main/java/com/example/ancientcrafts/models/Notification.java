package com.example.ancientcrafts.models;

public class Notification {
    public String buyerName;
    public String productName;
    public long   timeMillis;

    public Notification() {}
    public Notification(String buyer, String product) {
        this.buyerName   = buyer;
        this.productName = product;
        this.timeMillis  = System.currentTimeMillis();
    }
}

