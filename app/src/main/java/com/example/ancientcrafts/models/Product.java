package com.example.ancientcrafts.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private int id;
    private String name;
    private String imageName;
    private double price;
    private double originalPrice;
    private int soldCount;
    private float rating;
    private int reviewCount;
    private String description;
    private String deliveryEstimate;
    private String sellerUid; // NEW FIELD
    private int stock;           // NEW: stock quantity
    private long timestamp;      // NEW: date/time when product was added


    // No-argument constructor required for Firebase
    public Product() {
        // Default constructor for Firebase
    }

    // Constructor for dashboard or simple usage
    public Product(int id, String name, String imageName, double price, String sellerUid) {
        this.id = id;
        this.name = name;
        this.imageName = imageName;
        this.price = price;
        this.originalPrice = price * 1.5;
        this.soldCount = 0;
        this.rating = 4.0f;
        this.reviewCount = 0;
        this.description = "Premium quality product";
        this.deliveryEstimate = "3-5 days";
        this.sellerUid = sellerUid;
        this.stock = stock;
        this.timestamp = timestamp;
    }

    // Full constructor
    public Product(int id, String name, String imageName, double price,
                   double originalPrice, int soldCount, float rating,
                   int reviewCount, String description, String deliveryEstimate,
                   String sellerUid) {
        this.id = id;
        this.name = name;
        this.imageName = imageName;
        this.price = price;
        this.originalPrice = originalPrice;
        this.soldCount = soldCount;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.description = description;
        this.deliveryEstimate = deliveryEstimate;
        this.sellerUid = sellerUid;
    }

    // Parcelable implementation
    protected Product(Parcel in) {
        id = in.readInt();
        name = in.readString();
        imageName = in.readString();
        price = in.readDouble();
        originalPrice = in.readDouble();
        soldCount = in.readInt();
        rating = in.readFloat();
        reviewCount = in.readInt();
        description = in.readString();
        deliveryEstimate = in.readString();
        sellerUid = in.readString(); // NEW
        stock = in.readInt();
        timestamp = in.readLong();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(imageName);
        dest.writeDouble(price);
        dest.writeDouble(originalPrice);
        dest.writeInt(soldCount);
        dest.writeFloat(rating);
        dest.writeInt(reviewCount);
        dest.writeString(description);
        dest.writeString(deliveryEstimate);
        dest.writeString(sellerUid); // NEW
        dest.writeInt(stock);
        dest.writeLong(timestamp);
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getImageName() { return imageName; }
    public double getPrice() { return price; }
    public double getOriginalPrice() { return originalPrice; }
    public int getSoldCount() { return soldCount; }
    public float getRating() { return rating; }
    public int getReviewCount() { return reviewCount; }
    public String getDescription() { return description; }
    public String getDeliveryEstimate() { return deliveryEstimate; }
    public String getSellerUid() { return sellerUid; } // NEW

    // Setters
    public void setSellerUid(String sellerUid) {
        this.sellerUid = sellerUid;
    }
    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    // Helper method for image URL
    public String getImageUrl() {
        return "http://192.168.8.38/AncientCrafts_productImg/" + imageName;
    }
}
