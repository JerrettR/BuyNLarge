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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buynlarge.DB.AppDataBase;
import com.example.buynlarge.DB.OrderDAO;
import com.example.buynlarge.databinding.ActivityOrdersBinding;

import java.util.List;

public class OrdersActivity extends AppCompatActivity {
    ActivityOrdersBinding mOrdersBinding;
    private OrderDAO mOrderDAO;
    private List<Order> mOrderList;
    private TextView mOrdersList_TextView;
    private Spinner mOrderSpinner;
    private Button mCancelOrder_Button;
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

        displayOrders();

        displayOrdersSpinner();
    }

    private void displayOrders(){
        StringBuilder sb = new StringBuilder();
        for(Order order : mOrderDAO.getAllOrders()){
            sb.append(order);
        }
        mOrdersList_TextView.setText(sb);
        mOrdersList_TextView.setMovementMethod(new ScrollingMovementMethod());
    }

    private void displayOrdersSpinner(){
        mOrderList = mOrderDAO.getAllOrders();
        if(! mOrderList.isEmpty()){
            ArrayAdapter<Order> adapter = new ArrayAdapter<Order>(this, android.R.layout.simple_expandable_list_item_1, mOrderList);
            adapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
            mOrderSpinner.setAdapter(adapter);

            mOrderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedOrder = parent.getItemAtPosition(position).toString();

                    String[] wordArray = selectedOrder.trim().split("Order ID: ", 0);
                    int orderId = Integer.parseInt(wordArray[0]);
                    System.out.println("orderId: " + orderId);
                    Toast.makeText(parent.getContext(), "Selected: " + selectedOrder, Toast.LENGTH_LONG).show();
//                    Order order = mOrderDAO.getOrderById(orderId);

//                    mCancelOrder_Button.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            mOrderDAO.delete(order);
//                            Toast.makeText(OrdersActivity.this, "Cancelled Order: " + selectedOrder, Toast.LENGTH_LONG).show();
//                            Intent intent = OrdersActivity.getIntent(getApplicationContext());
//                            startActivity(intent);
//                        }
//                    });
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, OrdersActivity.class);
        return intent;
    }
}