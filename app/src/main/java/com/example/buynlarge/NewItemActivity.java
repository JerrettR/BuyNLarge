package com.example.buynlarge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.buynlarge.DB.AppDataBase;
import com.example.buynlarge.DB.ItemDAO;
import com.example.buynlarge.DB.UserDAO;
import com.example.buynlarge.databinding.ActivityNewItemBinding;

import java.util.List;

public class NewItemActivity extends AppCompatActivity {
    private static final String USER_ID_KEY = "com.example.buynlarge.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.buynlarge.PREFERENCES_KEY";
    private UserDAO mUserDAO;
    private int mUserId = -1;
    private User mUser;
    private SharedPreferences mPreferences = null;
    private EditText mItemNameInput, mPriceInput, mQtyInput, mDescriptionInput;
    private Button mAddItem_Button;
    private ImageButton mBackButton;
    private ActivityNewItemBinding mNewItemBinding;
    private ItemDAO mItemDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        mNewItemBinding = ActivityNewItemBinding.inflate(getLayoutInflater());
        View view = mNewItemBinding.getRoot();
        setContentView(view);
        mItemDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME).allowMainThreadQueries().build().getItemDAO();

        mItemNameInput = mNewItemBinding.itemNameInput;
        mPriceInput = mNewItemBinding.priceInput;
        mQtyInput = mNewItemBinding.qtyInput;
        mDescriptionInput = mNewItemBinding.descriptionInput;
        mAddItem_Button = mNewItemBinding.addItemButton;
        mBackButton = mNewItemBinding.backButton;

        getDatabase();

        checkForUser();

        addUserToPreference(mUserId);

        loginUser(mUserId);

        addItem();

        pressBackButton();
    }

    private void getDatabase(){
        mUserDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME).allowMainThreadQueries().build().getUserDAO();
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

    private void addItem(){
        mAddItem_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = mItemNameInput.getText().toString();
                double price = Double.parseDouble(mPriceInput.getText().toString());
                int quantity = Integer.parseInt(mQtyInput.getText().toString());
                String description = mDescriptionInput.getText().toString();

                Item newItem = new Item(itemName,price,quantity,description);
                mItemDAO.insert(newItem);
                Toast.makeText(NewItemActivity.this, "Item successfully created for: " + itemName, Toast.LENGTH_LONG).show();
                Intent intent = ItemsActivity.getIntent(getApplicationContext(),mUser.getUserId());
                startActivity(intent);
            }
        });
    }

    private void pressBackButton() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ItemsActivity.getIntent(getApplicationContext(),mUser.getUserId());
                startActivity(intent);
            }
        });
    }

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, NewItemActivity.class);
        return intent;
    }
}