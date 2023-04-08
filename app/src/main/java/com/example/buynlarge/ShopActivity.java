package com.example.buynlarge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.buynlarge.DB.AppDataBase;
import com.example.buynlarge.DB.ItemDAO;
import com.example.buynlarge.databinding.ActivityShopBinding;

public class ShopActivity extends AppCompatActivity {
    private ActivityShopBinding mShopBinding;
    private SearchView mShopSearch;
    private TextView mSearchResults;
    private ItemDAO mItemDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        mShopBinding = ActivityShopBinding.inflate(getLayoutInflater());
        View view = mShopBinding.getRoot();
        setContentView(view);
        mItemDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME).allowMainThreadQueries().build().getItemDAO();

        mShopSearch = mShopBinding.ShopSeachView;
        mSearchResults = mShopBinding.SearchResultsTextView;

        setShopSearch();
    }

    private void setShopSearch(){
        mShopSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onQueryTextSubmit(String query) {
                Item item = mItemDAO.getItemByItemName(query);
                mSearchResults.setText("Item Name: " + item.getItemName() + "\n" +
                                       "Price: " + item.getPrice() + "\n" +
                                       "Qty: " + item.getQuantity() + "\n" +
                                       "Description: " + item.getDescription());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
    }

    private void buyItem(){

    }

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, ShopActivity.class);
        return intent;
    }
}