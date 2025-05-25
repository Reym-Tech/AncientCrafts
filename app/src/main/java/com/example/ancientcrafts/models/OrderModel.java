package com.example.ancientcrafts.models;

public class OrderModel {
    private String orderId;
    private String buyerUid;
    private String sellerUid;
    private String productName;
    private String productImageUrl;
    private double price;
    private int quantity;
    private double totalPrice;
    private String orderStatus;
    private long timestamp;

    public OrderModel() {
        // Required for Firebase
    }

    public OrderModel(String orderId, String buyerUid, String sellerUid, String productName,
                      String productImageUrl, double price, int quantity,
                      double totalPrice, String orderStatus, long timestamp) {
        this.orderId = orderId;
        this.buyerUid = buyerUid;
        this.sellerUid = sellerUid;
        this.productName = productName;
        this.productImageUrl = productImageUrl;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getOrderId() { return orderId; }
    public String getBuyerUid() { return buyerUid; }
    public String getSellerUid() { return sellerUid; }
    public String getProductName() { return productName; }
    public String getProductImageUrl() { return productImageUrl; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public double getTotalPrice() { return totalPrice; }
    public String getOrderStatus() { return orderStatus; }
    public long getTimestamp() { return timestamp; }

    public void setOrderId(String orderId) { this.orderId = orderId; }
    public void setBuyerUid(String buyerUid) { this.buyerUid = buyerUid; }
    public void setSellerUid(String sellerUid) { this.sellerUid = sellerUid; }
    public void setProductName(String productName) { this.productName = productName; }
    public void setProductImageUrl(String productImageUrl) { this.productImageUrl = productImageUrl; }
    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
