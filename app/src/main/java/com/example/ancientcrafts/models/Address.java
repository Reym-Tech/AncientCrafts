package com.example.ancientcrafts.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

public class Address implements Parcelable {
    private String id;
    private String fullAddress;

    public Address(String fullAddress) {
        this.id = UUID.randomUUID().toString();
        this.fullAddress = fullAddress;
    }

    protected Address(Parcel in) {
        id = in.readString();
        fullAddress = in.readString();
    }

    public String getId() {
        return id;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(fullAddress);
    }
}
