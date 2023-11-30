package com.example.loginanimation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity implements MyAdapter.ActivityFinishListener{

    MyAdapter myAdapter;
    RecyclerView r_v_cart;

    String[] items;
    SharedPreferences sharedPreferences;


    Button btnPayment;
    CartActivity cartActivity = this;
    ArrayList<String> c_names,c_image;
    ArrayList<Integer> c_price = new ArrayList<>();
    ArrayList<Integer> c_qty;

    Double grand= 0.0;
    TextView txtSub,txtTax,txtTotal;
    double total=0,tax=0;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        r_v_cart = findViewById(R.id.r_v_cart);
        txtSub = findViewById(R.id.txtSub);
        txtTax = findViewById(R.id.txtTax);
        btnPayment = findViewById(R.id.btnPayment);
        txtTotal = findViewById(R.id.txtTotal);

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecieptActivity.class);
                items = new String[c_price.size()];

                for (int i =0 ; i<c_price.size();i++)
                {
                    items[i] = ""+(i+1)+".   " + c_names.get(i)+"  X    "+c_qty.get(i);
                }
                intent.putExtra("Items",items);
                if (items.length > 0) {
                    Toast.makeText(getApplicationContext(), "" + grand, Toast.LENGTH_SHORT).show();
                    intent.putExtra("itemPrice", grand);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(cartActivity, "The cart is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });


        SwipeCallback swipeCallback = new SwipeCallback(new SwipeListener() {
            @Override
            public void onSwipeRight(int position) {
                //Toast.makeText(CartActivity.this, ""+position, Toast.LENGTH_SHORT).show();
                whenSwiped(position);
            }
        });


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeCallback);
        itemTouchHelper.attachToRecyclerView(r_v_cart);
        SharedPreferences sharedPreferences = getSharedPreferences("MyCart", Context.MODE_PRIVATE);
        Gson gson = new Gson();

        String namesJson = sharedPreferences.getString("c_names",null);
        String priceJson = sharedPreferences.getString("c_price", null);
        String qtyJson = sharedPreferences.getString("c_qty", null);
        String imageJson = sharedPreferences.getString("c_image", null);

        if (namesJson != null && priceJson != null && qtyJson != null) {
            Type stringListType = new TypeToken<ArrayList<String>>() {}.getType();
            Type intListType = new TypeToken<ArrayList<Integer>>() {}.getType();

            c_names = gson.fromJson(namesJson, stringListType);
            c_image = gson.fromJson(imageJson, stringListType);
            c_price = gson.fromJson(priceJson, intListType);
            c_qty = gson.fromJson(qtyJson, intListType);
        }
        else{
            c_price.add(0);
            btnPayment.setClickable(false);
        }
        myAdapter = new MyAdapter(cartActivity,this,c_names,c_price,c_qty,c_image);

        if (c_price.get(0).equals(0))
        {
            Toast.makeText(this, "Nothing in the cart", Toast.LENGTH_SHORT).show();
        }
        else {
            for (int i = 0; i < c_price.size(); i++)
                total += c_price.get(i);
            txtSub.setText(total + " ₹");
            tax = total * 11.0 / 100;
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            String formattedTax = decimalFormat.format(tax);
            txtTax.setText(formattedTax + " ₹");

            grand = Double.valueOf(formattedTax) + total;
            txtTotal.setText(grand + " ₹");
            myAdapter.notifyDataSetChanged();
            r_v_cart.setAdapter(myAdapter);
            r_v_cart.setLayoutManager(new LinearLayoutManager(this));
        }

        BottomNavigationView my_navigator;
        my_navigator = findViewById(R.id.my_navigator);
        int account = R.id.account,home = R.id.home,menu = R.id.menu,cart = R.id.cart;
        my_navigator.setSelectedItemId(R.id.cart);
        my_navigator.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == menu)
                {
                    Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if (item.getItemId() == account) {
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(intent);
                    finish();

                }
                else if (item.getItemId() == home)
                {
                    Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                    startActivity(intent);
                    finish();
                }

                return true;
            }
        });
    }

    void afterSwipe(int position,Context context)
    {
        // Initialize the TextViews from the correct layout resource
        sharedPreferences = context.getSharedPreferences("MyCart", Context.MODE_PRIVATE);
        String priceJson = sharedPreferences.getString("c_price", null);
        Gson gson = new Gson();

        if (priceJson != null) {
            Type intListType = new TypeToken<ArrayList<Integer>>() {
            }.getType();
            c_price = gson.fromJson(priceJson, intListType);

//            Toast.makeText(context, "After Swipe: "+c_image.size(), Toast.LENGTH_SHORT).show();
            double t = 0,tx,gt;
            for (int i=0;i<c_price.size();i++)
                t += c_price.get(i);

            txtSub.setText(t+" ₹");
            tx = t * 11.0 /100;

            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            String formattedTax = decimalFormat.format(tx);
            txtTax.setText(formattedTax + " ₹");

            gt = Double.valueOf(formattedTax ) + t;
            grand = gt;
            txtTotal.setText(gt+" ₹");
        }
        else
        {
            btnPayment.setClickable(false);
        }
    }

    void updateQty(int qty)
    {}

    void whenSwiped(int position)
    {
        sharedPreferences = getSharedPreferences("MyCart", Context.MODE_PRIVATE);
        Gson gson = new Gson();

        c_image.remove(position);
        c_names.remove(position);
        c_price.remove(position);
        c_qty.remove(position);

        r_v_cart.getAdapter().notifyItemRemoved(position);
        String updatedNamesJson = gson.toJson(c_names);
        String updatedPriceJson = gson.toJson(c_price);
        String updatedQtyJson = gson.toJson(c_qty);
        String updatedImageJson = gson.toJson(c_image);

// Save the updated ArrayLists back to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("c_names", updatedNamesJson);
        editor.putString("c_price", updatedPriceJson);
        editor.putString("c_qty", updatedQtyJson);
        editor.putString("c_image",updatedImageJson);
        editor.apply();

        afterSwipe(position,getApplicationContext());
        myAdapter = new MyAdapter(cartActivity,getApplicationContext(),c_names,c_price,c_qty,c_image);

        r_v_cart.setAdapter(myAdapter);
        r_v_cart.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
    public void finishActivity() {
        finish(); // Finish the current activity
    }
}
