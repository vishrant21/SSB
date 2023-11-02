package com.example.loginanimation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MenuActivity extends AppCompatActivity {

    RecyclerAdapterMenu recyclerAdapterMenu;
    RecyclerView RV_Menu;
    String items[];
    int itemImg[];
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        RV_Menu = findViewById(R.id.RV_Menu);

        items = new String[]{"Indian Snacks","Ice Cream","Drinks","Dairy","Packed Goods"};

        itemImg = new int[]{R.drawable.snack,R.drawable.icecream,R.drawable.drinks,R.drawable.dairy,R.drawable.goods};


        recyclerAdapterMenu = new RecyclerAdapterMenu(this,items,itemImg);
        RV_Menu.setAdapter(recyclerAdapterMenu);
        RV_Menu.setLayoutManager(new LinearLayoutManager(this));

        BottomNavigationView my_navigator;
        my_navigator = findViewById(R.id.my_navigator);
        int account = R.id.account,home = R.id.home,menu = R.id.menu,cart = R.id.cart;
        my_navigator.setSelectedItemId(R.id.menu);
        my_navigator.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == home)
                {
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
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