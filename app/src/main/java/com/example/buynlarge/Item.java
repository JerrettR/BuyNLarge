package com.example.buynlarge;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.buynlarge.DB.AppDataBase;

import java.util.Date;

@Entity(tableName = AppDataBase.ITEM_TABLE)
public class Item {

    @PrimaryKey(autoGenerate = true)
    private int mItemId;
    private String mItemName;
    private double mPrice;
    private int mQuantity;
    private String mDescription;

    public Item(String itemName, double price, int quantity, String description) {
        mItemName = itemName;
        mPrice = price;
        mQuantity = quantity;
        mDescription = description;
    }

    @Override
    public String toString() {
        return "Item Name: " + mItemName + "\n" +
                "Price: " + mPrice + "\n" +
                "Qty: " + mQuantity + "\n" +
                "Description: " + mDescription + "\n" +
                "___________________________________________\n";
    }

    public int getItemId() {
        return mItemId;
    }

    public void setItemId(int itemId) {
        mItemId = itemId;
    }

    public String getItemName() {
        return mItemName;
    }

    public void setItemName(String itemName) {
        mItemName = itemName;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double price) {
        mPrice = price;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
        mQuantity = quantity;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }
}
