package com.example.buynlarge;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.runner.RunWith;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.buynlarge.DB.AppDataBase;
import com.example.buynlarge.DB.UserDAO;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class UsersDatabaseTest extends AppCompatActivity {
    User testUser = null;
    User testAdmin = null;
    UserDAO mUserDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME).allowMainThreadQueries().build().getUserDAO();

    @Before
    public void setUp(){
        System.out.println("Before each");
        testUser = new User("TestUser", "TestPassword", false);
        testAdmin = new User("TestAdmin", "TestPassword", true);
    }

    @After
    public void tearDown(){
        System.out.println("After each");
    }

    @Test
    public void insert(){
        System.out.println("insert test");
        assertEquals("TestUser", testUser.getUsername());
        assertEquals("TestPassword", testUser.getPassword());
        assertEquals(false, testUser.isAdmin());

        assertEquals("TestAdmin", testAdmin.getUsername());
        assertEquals("TestPassword", testAdmin.getPassword());
        assertEquals(true, testAdmin.isAdmin());

        List<User> usersList = mUserDAO.getAllUsers();
        System.out.println("usersList size: " + usersList.size());
    }
}
