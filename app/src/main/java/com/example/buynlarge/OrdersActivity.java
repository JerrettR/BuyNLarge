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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buynlarge.DB.AppDataBase;
import com.example.buynlarge.DB.ItemDAO;
import com.example.buynlarge.DB.OrderDAO;
import com.example.buynlarge.DB.UserDAO;
import com.example.buynlarge.databinding.ActivityOrdersBinding;

import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.buynlarge.userIdKey";
    private static final String PREFERENCES_KEY = "com.example.buynlarge.PREFERENCES_KEY";
    private UserDAO mUserDAO;
    private int mUserId = -1;
    private User mUser;
    private SharedPreferences mPreferences = null;
    ActivityOrdersBinding mOrdersBinding;
    private OrderDAO mOrderDAO;
    private ItemDAO mItemDAO;
    private List<Order> mOrderList;
    private TextView mOrdersList_TextView;
    private Spinner mOrderSpinner;
    private Button mCancelOrder_Button;
    private ImageButton mBackButton;
    private ArrayAdapter<String> adapter;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        mOrdersBinding = ActivityOrdersBinding.inflate(getLayoutInflater());
        View view = mOrdersBinding.getRoot();
        setContentView(view);
        mOrderDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME).allowMainThreadQueries().build().getOrderDAO();

        mOrdersList_TextView = mOrdersBinding.ordersListTextView;
        mOrderSpinner = mOrdersBinding.ordersSpinner;
        mCancelOrder_Button = mOrdersBinding.cancelOrderButton;
        mBackButton = mOrdersBinding.backButton;

        getDatabase();

        checkForUser();

        addUserToPreference(mUserId);

        loginUser(mUserId);

        displayOrders();

        displayOrdersSpinner();

        pressBackButton();
    }

    private void getDatabase(){
        mUserDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME).allowMainThreadQueries().build().getUserDAO();
        mItemDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME).allowMainThreadQueries().build().getItemDAO();
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

    private void displayOrders(){
        String username = mUser.getUsername();
        if(mUser.isAdmin()) {
            StringBuilder sb = new StringBuilder();
            for (Order order : mOrderDAO.getAllOrders()) {
                sb.append(order);
            }
            mOrdersList_TextView.setText(sb);
            mOrdersList_TextView.setMovementMethod(new ScrollingMovementMethod());
        }else{
            StringBuilder sb = new StringBuilder();
            for (Order order : mOrderDAO.getUserOrdersByUsername(username)) {
                sb.append(order);
            }
            mOrdersList_TextView.setText(sb);
            mOrdersList_TextView.setMovementMethod(new ScrollingMovementMethod());
        }
    }

    private void displayOrdersSpinner(){
        if(mUser.isAdmin()) {
            mOrderList = mOrderDAO.getAllOrders();
            if (!mOrderList.isEmpty()) {
                ArrayAdapter<Order> adapter = new ArrayAdapter<Order>(this, android.R.layout.simple_expandable_list_item_1, mOrderList);
                adapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
                mOrderSpinner.setAdapter(adapter);

                mOrderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedOrder = parent.getItemAtPosition(position).toString();

                        String[] wordArray = selectedOrder.trim().split("Order ID: ", 0);
                        String[] wordArray1 = wordArray[1].trim().split("Item Name: ", 0);

                        System.out.println("wordArray1[0]: " + wordArray1[0]);
                        System.out.println("wordArray1[1]: " + wordArray1[1]);

                        String[] wordArray2 = wordArray1[1].trim().split("Total Price: ", 0);
                        wordArray2[0] = wordArray2[0].replaceAll("[\n]", "");
                        Item item = mItemDAO.getItemByItemName(wordArray2[0]);

                        System.out.println("wordArray2[0]: " + wordArray2[0]);
                        System.out.println("wordArray2[1]: " + wordArray2[1]);
                        System.out.println("item: " + item);

                        int orderId = Integer.parseInt(wordArray1[0].trim());
                        System.out.println("orderId: " + orderId);
                        Toast.makeText(parent.getContext(), "Selected: " + selectedOrder, Toast.LENGTH_LONG).show();
                        Order order = mOrderDAO.getOrderById(orderId);

                        mCancelOrder_Button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mOrderDAO.delete(order);
                                Toast.makeText(OrdersActivity.this, "Cancelled Order: " + selectedOrder, Toast.LENGTH_LONG).show();
                                item.setQuantity(item.getQuantity()+1);
                                mItemDAO.update(item);
                                Intent intent = OrdersActivity.getIntent(getApplicationContext(), mUser.getUserId());
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        } else {
            mOrderList = mOrderDAO.getUserOrdersByUsername(mUser.getUsername());
            if (!mOrderList.isEmpty()) {
                ArrayAdapter<Order> adapter = new ArrayAdapter<Order>(this, android.R.layout.simple_expandable_list_item_1, mOrderList);
                adapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
                mOrderSpinner.setAdapter(adapter);

                mOrderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedOrder = parent.getItemAtPosition(position).toString();

                        String[] wordArray = selectedOrder.trim().split("Order ID: ", 0);
                        String[] wordArray1 = wordArray[1].trim().split("Item Name: ", 0);

                        System.out.println("wordArray1[0]: " + wordArray1[0]);
                        System.out.println("wordArray1[1]: " + wordArray1[1]);

                        String[] wordArray2 = wordArray1[1].trim().split("Total Price: ", 0);
                        wordArray2[0] = wordArray2[0].replaceAll("[\n]", "");
                        Item item = mItemDAO.getItemByItemName(wordArray2[0]);

                        System.out.println("wordArray2[0]: " + wordArray2[0]);
                        System.out.println("wordArray2[1]: " + wordArray2[1]);
                        System.out.println("item: " + item);

                        int orderId = Integer.parseInt(wordArray1[0].trim());
                        System.out.println("orderId: " + orderId);
                        Toast.makeText(parent.getContext(), "Selected: " + selectedOrder, Toast.LENGTH_LONG).show();
                        Order order = mOrderDAO.getOrderById(orderId);

                        mCancelOrder_Button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mOrderDAO.delete(order);
                                Toast.makeText(OrdersActivity.this, "Cancelled Order: " + selectedOrder, Toast.LENGTH_LONG).show();
                                if(item.getQuantity() <= 0){
                                    item.setQuantity(1);
                                    mItemDAO.update(item);
                                } else {
                                    item.setQuantity(item.getQuantity() + 1);
                                    mItemDAO.update(item);
                                }
                                Intent intent = OrdersActivity.getIntent(getApplicationContext(), mUser.getUserId());
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
        Intent intent = new Intent(context, OrdersActivity.class);
        intent.putExtra(USER_ID_KEY, userId);

        return intent;
    }
}