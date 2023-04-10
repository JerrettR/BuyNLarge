package com.example.buynlarge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buynlarge.DB.AppDataBase;
import com.example.buynlarge.DB.ItemDAO;
import com.example.buynlarge.DB.OrderDAO;
import com.example.buynlarge.DB.UserDAO;
import com.example.buynlarge.databinding.ActivityShopBinding;

public class ShopActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.buynlarge.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.buynlarge.PREFERENCES_KEY";
    private int mUserId = -1;
    private User mUser;
    private SharedPreferences mPreferences = null;
    private ActivityShopBinding mShopBinding;
    private SearchView mShopSearch;
    private TextView mSearchResults;
    private Button mBuy_Button;
    private ImageButton mBackButton;
    private ItemDAO mItemDAO;
    private OrderDAO mOrderDAO;
    private UserDAO mUserDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        mShopBinding = ActivityShopBinding.inflate(getLayoutInflater());
        View view = mShopBinding.getRoot();
        setContentView(view);
        mItemDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME).allowMainThreadQueries().build().getItemDAO();
        mOrderDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME).allowMainThreadQueries().build().getOrderDAO();
        mUserDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME).allowMainThreadQueries().build().getUserDAO();

        mShopSearch = mShopBinding.ShopSeachView;
        mSearchResults = mShopBinding.SearchResultsTextView;
        mBuy_Button = mShopBinding.BuyButton;
        mBackButton = mShopBinding.backButton;

        getDatabase();

        checkForUser();

        addUserToPreference(mUserId);

        loginUser(mUserId);

        setShopSearch();

        pressBackButton();
    }

    private void getDatabase(){
        mUserDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME).allowMainThreadQueries().build().getUserDAO();
    }

    private void checkForUser() {
        //Do we have a user in the intent?
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);

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

    private void setShopSearch(){
        mShopSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onQueryTextSubmit(String query) {
                Item item = mItemDAO.getItemByItemName(query);
                mSearchResults.setText("Item Name: " + item.getItemName() + "\n" +
                                       "Price: " + String.format("%,.2f",item.getPrice()) + "\n" +
                                       "Qty In Stock: " + item.getQuantity() + "\n" +
                                       "Description: " + item.getDescription());

                String itemName = item.getItemName();
                double itemTotal = item.getPrice();
                if(item != null){
                    buyItem(item, itemName,itemTotal);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void buyItem(Item item, String itemName, double itemTotal){
        mBuy_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order newOrder = new Order(itemName,itemTotal);
                mOrderDAO.insert(newOrder);
                Toast.makeText(ShopActivity.this, "Item ordered: " + itemName, Toast.LENGTH_LONG).show();
                item.setQuantity(item.getQuantity()-1);
                mItemDAO.update(item);
                Intent intent = OrdersActivity.getIntent(getApplicationContext(), mUser.getUserId());
                startActivity(intent);
            }
        });
    }

    private void pressBackButton() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MainActivity.getIntent(getApplicationContext(),mUser.getUserId());
                startActivity(intent);
            }
        });
    }

    public static Intent getIntent(Context context, int userId){
        Intent intent = new Intent(context, ShopActivity.class);
        intent.putExtra(USER_ID_KEY, userId);

        return intent;
    }
}