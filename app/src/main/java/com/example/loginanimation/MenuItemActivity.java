package com.example.loginanimation;

import android.annotation.SuppressLint;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    Button btnNewItem;
    TextView txtMenuItem,txtInfo;
    boolean isAdminLoggedIn;
    ArrayList<String> currentName = new ArrayList<>();
    ArrayList<String> currentPrice = new ArrayList<>();
    ArrayList<String> currentImage = new ArrayList<>();
    FirebaseDatabase instance = FirebaseDatabase.getInstance();

    MenuItemActivity menuItemActivity = this;
    MenuItemAdapter menuItemAdapter;
    SharedPreferences sharedPreferences;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item);
        r_v_menu_item = findViewById(R.id.r_v_menu_item);
        txtMenuItem = findViewById(R.id.txtMenuItem);
        btnNewItem = findViewById(R.id.btnNewItem);

        Intent intent = getIntent();
        txtInfo = findViewById(R.id.txtInfo);
        menuItem = intent.getStringExtra("MenuItem");
        DatabaseReference reference = instance.getReference().child(""+menuItem);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        isAdminLoggedIn = sharedPreferences.getBoolean("isAdminLoggedIn", false);


        if (isAdminLoggedIn)
        {
            txtInfo.setVisibility(View.VISIBLE);
            btnNewItem.setVisibility(View.VISIBLE);
        }
        else {}


        btnNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), AddNewItemActivity.class);
                    intent.putExtra("Category",menuItem);
                    startActivity(intent);
                    finish();
            }
        });
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
                    menuItemAdapter = new MenuItemAdapter(getApplicationContext(),breakfast,price,image,menuItemActivity,isAdminLoggedIn);
                    r_v_menu_item.setAdapter(menuItemAdapter);
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
    void afterLongClick(int position) {
        DatabaseReference reference = instance.getReference().child("" + menuItem);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Get all data as maps
                    ArrayList<String> nameData = (ArrayList<String>) dataSnapshot.child("Name").getValue();
                    ArrayList<Integer> priceData = (ArrayList<Integer>) dataSnapshot.child("Price").getValue();
                    ArrayList<String> imageData = (ArrayList<String>) dataSnapshot.child("Image").getValue();

                    // Check if the position is valid
                    if (position >= 0 && position < nameData.size() && position < priceData.size() && position < imageData.size()) {
                        // Remove the item at the specified position
                        nameData.remove(position);
                        priceData.remove(position);
                        imageData.remove(position);

                        // Re-enter the updated data
                        reference.child("Name").setValue(nameData);
                        reference.child("Price").setValue(priceData);
                        reference.child("Image").setValue(imageData);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });
    }
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
        finish();
        // This line is important to call the default behavior (finishing the current activity)
    }
}