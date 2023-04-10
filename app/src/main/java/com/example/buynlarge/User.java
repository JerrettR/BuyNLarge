package com.example.buynlarge;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.buynlarge.DB.AppDataBase;

import java.util.Date;

@Entity(tableName = AppDataBase.USER_TABLE)
public class User {

    @PrimaryKey(autoGenerate = true)
    private int mUserId;
    private String mUsername;
    private String mPassword;
    private boolean mIsAdmin;
    private Date mDate;

    public User(String username, String password, boolean isAdmin) {
        mUsername = username;
        mPassword = password;
        mIsAdmin = isAdmin;
        mDate = new Date();
    }

    @Override
    public String toString() {
        return "User ID: " + mUserId + "\n" +
                "Username: " + mUsername + "\n" +
                "Password: " + mPassword + "\n" +
                "_____________________________________________\n";
    }

    public boolean isAdmin() {
        return mIsAdmin;
    }

    public void setAdmin(boolean admin) {
        mIsAdmin = admin;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}
