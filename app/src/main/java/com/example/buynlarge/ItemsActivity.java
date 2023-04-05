package com.example.buynlarge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buynlarge.DB.AppDataBase;
import com.example.buynlarge.DB.ItemDAO;
import com.example.buynlarge.databinding.ActivityItemsBinding;

import java.util.List;

public class ItemsActivity extends AppCompatActivity {

    ActivityItemsBinding mItemsBinding;
    private ItemDAO mItemDAO;
    private List<Item> mItemList;
    private TextView mItemsList_TextView;
    private Spinner mItemSpinner;
    private Button mDeleteItem_Button, mAddItem_Button, mNewItem_Button;
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

        displayItems();

        displayItemsSpinner();

//        addItem();
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
                            Intent intent = ItemsActivity.getIntent(getApplicationContext());
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

//    public void addItem(){
//        mAddItem_Button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = NewItemActivity.getIntent(getApplicationContext());
//                startActivity(intent);
//            }
//        });
//    }

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, ItemsActivity.class);
        return intent;
    }
}