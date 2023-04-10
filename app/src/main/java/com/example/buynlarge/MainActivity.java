package com.example.buynlarge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buynlarge.DB.AppDataBase;
import com.example.buynlarge.DB.ItemDAO;
import com.example.buynlarge.DB.OrderDAO;
import com.example.buynlarge.DB.UserDAO;
import com.example.buynlarge.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.buynlarge.userIdKey";
    private static final String ITEM_ID_KEY = "com.example.buynlarge.itemIdKey";
    private static final String PREFERENCES_KEY = "com.example.buynlarge.PREFERENCES_KEY";
    ActivityMainBinding mMainBinding;
    private int mUserId = -1;
    private UserDAO mUserDAO;
    private List<User> mUserList;
    private int mItemId = -1;
    private ItemDAO mItemDAO;
    private List<Item> mItemList;
    private OrderDAO mOrderDAO;
    private SharedPreferences mPreferences = null;
    private User mUser;
    private TextView mWelcomeUser;
    private Button mAdmin_Button;
    private Button mOrders_Button;
    private Button mShop_Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mMainBinding.getRoot();
        setContentView(view);

        mWelcomeUser = mMainBinding.welcomeUserTextView;
        mAdmin_Button = mMainBinding.adminButton;
        mOrders_Button = mMainBinding.ordersButton;
        mShop_Button = mMainBinding.shopButton;

        getDatabase();

        checkForUser();

        addUserToPreference(mUserId);

        loginUser(mUserId);

        hideAdminMenu();

        checkForItems();

        checkForOrders();

        pressAdminButton();

        pressShopButton();

        pressOrdersButton();

        displayWelcomeMessage();
    }

    private void getDatabase(){
        mUserDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME).allowMainThreadQueries().build().getUserDAO();
        mItemDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME).allowMainThreadQueries().build().getItemDAO();
        mOrderDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME).allowMainThreadQueries().build().getOrderDAO();
    }

    private void checkForUser() {
        //Do we have a user in the intent?
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        System.out.println("mUserId: " + mUserId);

        //Do we have a user in the preferences?
        if(mUserId != -1){
            return;
        }

        if(mPreferences == null) {
            getPrefs();
        }
        mUserId = mPreferences.getInt(USER_ID_KEY, -1);

        if(mUserId != -1){
            return;
        }

        //Do we have any users at all?
        List<User> users = mUserDAO.getAllUsers();
        if(users.size() <= 0){
            User defaultUser = new User("testuser1","testuser1", false);
            User admin = new User("admin2","admin2", true);
            mUserDAO.insert(defaultUser,admin);
        }

        Intent intent = LoginActivity.getIntent(this);
        startActivity(intent);
    }

    private void addUserToPreference(int userId) {
        if(mPreferences == null){
            getPrefs();
        }
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(USER_ID_KEY, userId);
    }

    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    private void loginUser(int userId) {
        mUser = mUserDAO.getUserByUserId(userId);

        invalidateOptionsMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(mUser != null){
            MenuItem item = menu.findItem(R.id.logout_menu);
            item.setTitle(mUser.getUsername());
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout_menu:
                logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logoutUser(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage(R.string.logout);

        alertBuilder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearUserFromIntent();
                        clearUserFromPref();
                        mUserId = -1;
                        checkForUser();
                        Toast.makeText(MainActivity.this, "Successfully signed out", Toast.LENGTH_SHORT).show();
                    }
                });
        alertBuilder.setNegativeButton(getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "Did not sign out", Toast.LENGTH_SHORT).show();
                    }
                });
        alertBuilder.create().show();
    }

    private void clearUserFromIntent(){
        getIntent().putExtra(USER_ID_KEY, -1);
    }

    private void clearUserFromPref(){
        addUserToPreference(-1);
    }

    private void pressAdminButton(){
        mAdmin_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AdminActivity.getIntent(getApplicationContext(), mUser.getUserId());
                startActivity(intent);
            }
        });
    }

    private void pressShopButton(){
        mShop_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ShopActivity.getIntent(getApplicationContext(),mUser.getUserId());
                startActivity(intent);
            }
        });
    }

    private void pressOrdersButton(){
        mOrders_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = OrdersActivity.getIntent(getApplicationContext(), mUser.getUserId());
                startActivity(intent);
            }
        });
    }

    private void displayWelcomeMessage(){
        if(mUser != null) {
            String welcomeMessage = getString(R.string.welcome_username, mUser.getUsername());
            mWelcomeUser.setText(welcomeMessage);
        }
    }

    private void hideAdminMenu(){
        if(mUser != null && mUser.isAdmin()){
            mAdmin_Button.setVisibility(View.VISIBLE);
        }else{
            mAdmin_Button.setVisibility(View.GONE);
        }
    }

    private void checkForItems(){
        List<Item> items = mItemDAO.getAllItems();
        if(items.size() <= 0){
            Item item1 = new Item("Wall-E",100.00, 2, "Cleans the earth during an apocalypse.");
            Item item2 = new Item("Cheeseburger Shake",100.00, 1, "Your favorite meal, in a cup!");
            Item item3 = new Item("Otto-Pilot Robot",100.00, 1, "Drives any spaceship autonomously, may lead a mutiny.");
            Item item4 = new Item("Lightning McQueen Race Car",100.00, 1, "Ka-Chow!");
            mItemDAO.insert(item1,item2,item3,item4);
        }
    }

    private void checkForOrders(){
        List<Order> orders = mOrderDAO.getAllOrders();
        if(orders.size() <= 0){
            Order order1 = new Order("Axiom Spaceship", 2000000.00);
            mOrderDAO.insert(order1);
        }
    }

    public static Intent getIntent(Context context, int userId){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(USER_ID_KEY, userId);

        return intent;
    }
}