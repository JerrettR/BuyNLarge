package com.example.buynlarge.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.buynlarge.Order;

import java.util.List;

@Dao
public interface OrderDAO {

    @Insert
    void insert(Order... orders);

    @Update
    void update(Order... orders);

    @Delete
    void delete(Order order);

    @Query("SELECT * FROM " + AppDataBase.ORDER_TABLE)
    List<Order> getOrderLogs();

    @Query("SELECT * FROM " + AppDataBase.ORDER_TABLE + " WHERE mOrderId = :orderId ORDER BY mOrderId")
    Order getOrderById(int orderId);

    @Query("SELECT * FROM " + AppDataBase.ORDER_TABLE + " ORDER BY mOrderId")
    List<Order> getAllOrders();

    @Query("SELECT * FROM " + AppDataBase.ORDER_TABLE + " WHERE mUsername = :username ORDER BY mOrderId")
    List<Order> getAllOrdersByUsername(String username);

    @Query("SELECT * FROM " + AppDataBase.ORDER_TABLE + " WHERE mItemName = :itemName")
    List<Order> getOrdersByItemName(String itemName);

    @Query("SELECT * FROM " + AppDataBase.ORDER_TABLE + " WHERE mUsername = :username ORDER BY mOrderId")
    List<Order> getUserOrdersByUsername(String username);
}
