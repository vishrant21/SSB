package com.example.loginanimation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MenuItemActivity extends AppCompatActivity {


    RecyclerView r_v_menu_item;
    String menuItem;
    TextView txtMenuItem;
    FirebaseDatabase instance = FirebaseDatabase.getInstance();

    RecyclerAdapter recyclerAdapter;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item);
        r_v_menu_item = findViewById(R.id.r_v_menu_item);
        txtMenuItem = findViewById(R.id.txtMenuItem);
        Intent intent = getIntent();
        menuItem = intent.getStringExtra("MenuItem");
        DatabaseReference reference = instance.getReference().child(""+menuItem);


        txtMenuItem.setText(""+menuItem);
        reference.addValueEventListener(new ValueEventListener() {
            String image[],breakfast[];
            int price[];
            ArrayList<String> bf = new ArrayList<>();
            ArrayList<Integer> price_a = new ArrayList<>();
            ArrayList<String> image_a = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                    bf = (ArrayList<String>) snapshot.child("Name").getValue();
                    image_a = (ArrayList<String>) snapshot.child("Image").getValue();
                    price_a = (ArrayList<Integer>) snapshot.child("Price").getValue();

                    breakfast = new String[bf.size()];
                    price = new int[price_a.size()];
                    image = new String[image_a.size() ];
                    Toast.makeText(getApplicationContext(), ""+bf, Toast.LENGTH_SHORT).show();

                    for (int i=0;i<bf.size();i++)
                    {

                        breakfast[i] = bf.get(i);
                        price[i]= Integer.parseInt(String.valueOf(price_a.get(i)));
                        image[i]= image_a.get(i);
                    }
                    recyclerAdapter = new RecyclerAdapter(getApplicationContext(),breakfast,price,image);
                    r_v_menu_item.setAdapter(recyclerAdapter);
                    r_v_menu_item.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                else {
                    Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                    startActivity(intent);
                    finish();
                }

                return true;
            }
        });

    }
}