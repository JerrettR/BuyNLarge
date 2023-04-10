package com.example.buynlarge;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.buynlarge.DB.AppDataBase;

import java.util.Date;

@Entity(tableName = AppDataBase.ORDER_TABLE)
public class Order {

    @PrimaryKey(autoGenerate = true)
    private int mOrderId;
    private Date mDate;
    private String mItemName;
    private double mOrderTotal;
    private String mUsername;

    public Order(String itemName, double orderTotal, String username) {
        mOrderId = getOrderId();
        mItemName = itemName;
        mOrderTotal = orderTotal;
        mDate = new Date();
        mUsername = username;
    }

    @Override
    public String toString() {
        return "Order ID: " + mOrderId + "\n" +
                "Item Name: " + mItemName + "\n" +
                "Total Price: $" + String.format("%,.2f", mOrderTotal) + "\n" +
                "Ordered By: " + mUsername + "\n" +
                "Order Date: " + mDate + "\n" +
                "___________________________________________\n";
    }

    public int getOrderId() {
        return mOrderId;
    }

    public void setOrderId(int orderId) {
        mOrderId = orderId;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getItemName() {
        return mItemName;
    }

    public void setItemName(String itemName) {
        mItemName = itemName;
    }

    public double getOrderTotal() {
        return mOrderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        mOrderTotal = orderTotal;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }
}
