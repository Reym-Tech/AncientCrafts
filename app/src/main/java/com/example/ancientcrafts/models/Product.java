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

    // No-argument constructor required for Firebase
    public Product() {
        // Default constructor for Firebase
    }

    // Constructor for dashboard or minimal product info
    public Product(int id, String name, String imageName, double price) {
        this.id = id;
        this.name = name;
        this.imageName = imageName;
        this.price = price;
        this.originalPrice = price * 1.5; // Default mark-up
        this.soldCount = 0;
        this.rating = 4.0f;
        this.reviewCount = 0;
        this.description = "Premium quality product";
        this.deliveryEstimate = "3-5 days";
    }

    // Full constructor with all fields
    public Product(int id, String name, String imageName, double price,
                   double originalPrice, int soldCount, float rating,
                   int reviewCount, String description, String deliveryEstimate) {
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
    }

    // Parcelable constructor
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
    }

    // Parcelable creator
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

    // Parcelable methods
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
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageName() {
        return imageName;
    }

    public double getPrice() {
        return price;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public int getSoldCount() {
        return soldCount;
    }

    public float getRating() {
        return rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public String getDescription() {
        return description;
    }

    public String getDeliveryEstimate() {
        return deliveryEstimate;
    }

    // Helper method for loading image from server
    public String getImageUrl() {
        return "http://192.168.8.41/AncientCrafts_productImg/" + imageName;
    }
}
