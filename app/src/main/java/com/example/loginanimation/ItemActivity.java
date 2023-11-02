package com.example.loginanimation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ItemActivity extends AppCompatActivity {

    ImageView imgItem;
    int qty=1,price,posPrice,posQty,edit;
    TextView btnCart;

    String posName,posImage;
    int priceEach,position;
    ArrayList<String> c_names,c_image;
    ArrayList<Integer> c_price;
    ArrayList<Integer> c_qty;

    TextView btnMinus,btnPlus, txtQty, txtIprice,txtIname;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        btnMinus = findViewById(R.id.btnMinus);
        txtIname = findViewById(R.id.txtIname);
        txtIprice = findViewById(R.id.txtIprice);
        txtQty = findViewById(R.id.txtQty);
        btnPlus = findViewById(R.id.btnPlus);
        imgItem = findViewById(R.id.imgItem);
        btnCart = findViewById(R.id.btnCart);
        Intent intent = getIntent();
        position = intent.getIntExtra("position",-1);
        posQty = intent.getIntExtra("posQty",0);
        edit = intent.getIntExtra("edit",0);
        posImage = intent.getStringExtra("posImage");
        posPrice = intent.getIntExtra("posPrice",0);
        posName = intent.getStringExtra("posName");

        Picasso.get().load(posImage).into(imgItem);

        txtIprice.setText("Price: ₹ "+posPrice);
        if(edit == 1)
        {
            txtQty.setText(""+posQty);
            txtIprice.setText("Price: ₹ "+posPrice);
            priceEach = posPrice/posQty;
        }
        else {
            priceEach = posPrice;

        }
        txtIname.setText(""+posName);
        Toast.makeText(this, ""+price, Toast.LENGTH_SHORT).show();

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qty = Integer.parseInt(txtQty.getText().toString());

                if (qty > 1) {
                    posPrice = posPrice-priceEach;
                    qty = qty - 1;
                    txtQty.setText(String.valueOf(qty));

                    txtIprice.setText("Price: ₹ "+posPrice);
                }
                else
                    Toast.makeText(ItemActivity.this, "Order can't be less than 1", Toast.LENGTH_SHORT).show();

            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qty = Integer.parseInt(txtQty.getText().toString());
                qty = qty + 1;
                posPrice = posPrice+priceEach;
                txtIprice.setText("Price: ₹"+(posPrice));
                txtQty.setText(String.valueOf(qty));
            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getSharedPreferences("MyCart", Context.MODE_PRIVATE);
                Gson gson = new Gson();

// Retrieve the ArrayLists from SharedPreferences
                String namesJson = sharedPreferences.getString("c_names", null);
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
                } else {
                    // Initialize the ArrayLists if they don't exist in SharedPreferences
                    c_names = new ArrayList<>();
                    c_image = new ArrayList<>();
                    c_price = new ArrayList<>();
                    c_qty = new ArrayList<>();
                }


                if (edit == 1)
                {
                    c_price.set(position,posPrice);
                    c_qty.set(position,qty);
                    c_names.set(position,posName);
                    c_image.set(position,posImage);
                }
                else {
                    c_price.add(posPrice);
                    c_qty.add(qty);
                    c_names.add(posName);
                    c_image.add(posImage);
                }

// Convert the ArrayLists to JSON strings
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

                Intent intent1 = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent1);
                finish();
            }
        });

        BottomNavigationView my_navigator;
        my_navigator = findViewById(R.id.my_navigator);
        int account = R.id.account,home = R.id.home,menu = R.id.menu,cart = R.id.cart;
        my_navigator.setSelectedItemId(R.id.home);
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
                else if (item.getItemId() == cart)
                {
                    Intent intent = new Intent(getApplicationContext(),CartActivity.class);
                    startActivity(intent);
                    finish();
                }

                return true;
            }
        });
    }
}