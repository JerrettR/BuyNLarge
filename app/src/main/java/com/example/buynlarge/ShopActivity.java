package com.example.buynlarge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buynlarge.DB.AppDataBase;
import com.example.buynlarge.DB.ItemDAO;
import com.example.buynlarge.DB.OrderDAO;
import com.example.buynlarge.DB.UserDAO;
import com.example.buynlarge.databinding.ActivityShopBinding;

public class ShopActivity extends AppCompatActivity {
    private ActivityShopBinding mShopBinding;
    private SearchView mShopSearch;
    private TextView mSearchResults;
    private Button mBuy_Button;
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

        setShopSearch();
    }

    private void setShopSearch(){
        mShopSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onQueryTextSubmit(String query) {
                Item item = mItemDAO.getItemByItemName(query);
                mSearchResults.setText("Item Name: " + item.getItemName() + "\n" +
                                       "Price: " + String.format("%,.2f",item.getPrice()) + "\n" +
                                       "Qty: " + item.getQuantity() + "\n" +
                                       "Description: " + item.getDescription());

                String itemName = item.getItemName();
                double itemTotal = item.getPrice();
                if(item != null){
                    buyItem(itemName,itemTotal);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void buyItem(String itemName, double itemTotal){
        mBuy_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order newOrder = new Order(itemName,itemTotal);
                mOrderDAO.insert(newOrder);
                Toast.makeText(ShopActivity.this, "Item ordered: " + itemName, Toast.LENGTH_LONG).show();
                Intent intent = OrdersActivity.getIntent(getApplicationContext());
                startActivity(intent);
            }
        });
    }

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, ShopActivity.class);
        return intent;
    }
}