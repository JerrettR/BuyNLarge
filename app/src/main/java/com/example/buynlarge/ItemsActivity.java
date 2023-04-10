package com.example.buynlarge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buynlarge.DB.AppDataBase;
import com.example.buynlarge.DB.ItemDAO;
import com.example.buynlarge.DB.UserDAO;
import com.example.buynlarge.databinding.ActivityItemsBinding;

import java.util.List;

public class ItemsActivity extends AppCompatActivity {
    private static final String USER_ID_KEY = "com.example.buynlarge.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.buynlarge.PREFERENCES_KEY";
    private UserDAO mUserDAO;
    private int mUserId = -1;
    private User mUser;
    private SharedPreferences mPreferences = null;
    ActivityItemsBinding mItemsBinding;
    private ItemDAO mItemDAO;
    private List<Item> mItemList;
    private TextView mItemsList_TextView;
    private Spinner mItemSpinner;
    private Button mDeleteItem_Button, mAddItem_Button, mNewItem_Button;
    private ImageButton mBackButton;
    private ArrayAdapter<String> adapter;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        mItemsBinding = ActivityItemsBinding.inflate(getLayoutInflater());
        View view = mItemsBinding.getRoot();
        setContentView(view);
        mItemDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME).allowMainThreadQueries().build().getItemDAO();

        mItemsList_TextView = mItemsBinding.itemsListTextView;
        mItemSpinner = mItemsBinding.itemsSpinner;
        mDeleteItem_Button = mItemsBinding.deleteItemButton;
        mNewItem_Button = mItemsBinding.newItemButton;
        mBackButton = mItemsBinding.backButton;

        getDatabase();

        checkForUser();

        addUserToPreference(mUserId);

        loginUser(mUserId);

        displayItems();

        displayItemsSpinner();

        newItem();

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

    private void displayItems(){
        StringBuilder sb = new StringBuilder();
        for(Item item : mItemDAO.getAllItems()){
            sb.append(item);
        }
        mItemsList_TextView.setText(sb);
        mItemsList_TextView.setMovementMethod(new ScrollingMovementMethod());
    }

    private void displayItemsSpinner(){
        mItemList = mItemDAO.getAllItems();
        if(! mItemList.isEmpty()){
            ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(this, android.R.layout.simple_expandable_list_item_1, mItemList);
            adapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
            mItemSpinner.setAdapter(adapter);

            mItemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItem = parent.getItemAtPosition(position).toString();

                    String[] wordArray = selectedItem.trim().split("Item Name: ", 0);
                    String[] wordArray1 = wordArray[1].trim().split("Price: ", 0);
                    wordArray1[0] = wordArray1[0].replaceAll("[\n]", "");
                    Toast.makeText(parent.getContext(), "Selected: " + selectedItem, Toast.LENGTH_LONG).show();
                    Item item = mItemDAO.getItemByItemName(wordArray1[0]);

                    mDeleteItem_Button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mItemDAO.delete(item);
                            Toast.makeText(ItemsActivity.this, "Deleted item: " + selectedItem, Toast.LENGTH_LONG).show();
                            Intent intent = ItemsActivity.getIntent(getApplicationContext(),mUser.getUserId());
                            startActivity(intent);
                        }
                    });
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    public void newItem(){
        mNewItem_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = NewItemActivity.getIntent(getApplicationContext());
                startActivity(intent);
            }
        });
    }

    private void pressBackButton() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AdminActivity.getIntent(getApplicationContext(), mUser.getUserId());
                startActivity(intent);
            }
        });
    }

    public static Intent getIntent(Context context, int userId){
        Intent intent = new Intent(context, ItemsActivity.class);
        intent.putExtra(USER_ID_KEY, userId);

        return intent;
    }
}