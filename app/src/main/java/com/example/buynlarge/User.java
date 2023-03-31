package com.example.buynlarge;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.buynlarge.DB.AppDataBase;

import java.util.Date;

@Entity(tableName = AppDataBase.USER_TABLE)
public class User {

    @PrimaryKey(autoGenerate = true)
    private int mLogId;
    private String mUsername;
    private String mPassword;
    private Date mDate;

    public User(String username, String password) {
        mUsername = username;
        mPassword = password;
        mDate = new Date();
    }

    @Override
    public String toString() {
        return "Log #: " + mLogId + "\n" +
                "Username: " + mUsername + "\n" +
                "Password: " + mPassword + "\n" +
                "Date: " + mDate + "\n" +
                "------------------------------\n";
    }

    public int getLogId() {
        return mLogId;
    }

    public void setLogId(int logId) {
        mLogId = logId;
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
