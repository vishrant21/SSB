package com.example.loginanimation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    RecyclerView r_v_Breakfast,r_v_Snacks,r_v_Dairy,r_v_IceCream,r_v_Drinks,r_v_Packed;

    Handler handler;
    Runnable autoScrollRunnable;

    int[] images = {R.drawable.dhokla,R.drawable.kachori,R.drawable.samosa,R.drawable.poha,R.drawable.bhajiya,R.drawable.jalebi};
    private DatabaseReference snack1;

    ViewPager viewPager;
    TextView Test;


    RecyclerAdapter recyclerAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Test = findViewById(R.id.Test);
        r_v_Breakfast = findViewById(R.id.r_v_Breakfast);
        r_v_Dairy = findViewById(R.id.r_v_Dairy);
        r_v_IceCream = findViewById(R.id.r_v_Ice);
        r_v_Drinks = findViewById(R.id.r_v_Drinks);
        viewPager =findViewById(R.id.viewPager);
        r_v_Packed = findViewById(R.id.r_v_Packed);
        FirebaseDatabase instance = FirebaseDatabase.getInstance();

        ImagePagerAdapter adapter = new ImagePagerAdapter(this, images);
        viewPager.setAdapter(adapter);

//        viewPager.setPageTransformer(true, new CustomPageTransformer());
        handler = new Handler();
        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = viewPager.getCurrentItem();
                int nextItem = currentItem + 1;
                if (nextItem == adapter.getCount()) {
                        nextItem = 0;

                }
                viewPager.setCurrentItem(nextItem, true); // true for smooth scrolling
                handler.postDelayed(this, 2000); // 3000 milliseconds (3 seconds) delay for auto-scroll
            }
        };





        DatabaseReference reference = instance.getReference("Indian Snacks");
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
                Toast.makeText(HomeActivity.this, ""+bf, Toast.LENGTH_SHORT).show();

                for (int i=0;i<bf.size();i++)
                {

                    breakfast[i] = bf.get(i);
                    price[i]= Integer.parseInt(String.valueOf(price_a.get(i)));
                    image[i]= image_a.get(i);
                }
                recyclerAdapter = new RecyclerAdapter(getApplicationContext(),breakfast,price,image);
                r_v_Breakfast.setAdapter(recyclerAdapter);
                r_v_Breakfast.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        DatabaseReference reference2 = instance.getReference("Dairy");
        reference2.addValueEventListener(new ValueEventListener() {
            String dairy[],image[];
            int price[];
            ArrayList<String> bf = new ArrayList<>();
            ArrayList<Integer> price_a = new ArrayList<>();
            ArrayList<String> image_a = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                bf = (ArrayList<String>) snapshot.child("Name").getValue();
                image_a = (ArrayList<String>) snapshot.child("Image").getValue();
                price_a = (ArrayList<Integer>) snapshot.child("Price").getValue();

                dairy = new String[bf.size()];
                price = new int[price_a.size()];
                image = new String[image_a.size() ];
                Toast.makeText(HomeActivity.this, ""+price_a, Toast.LENGTH_SHORT).show();

                for (int i=0;i<bf.size();i++)
                {

                    dairy[i] = bf.get(i);
                    price[i]= Integer.parseInt(String.valueOf(price_a.get(i)));
                    image[i]= image_a.get(i);
                }

                Toast.makeText(HomeActivity.this, ""+price_a, Toast.LENGTH_SHORT).show();
                recyclerAdapter = new RecyclerAdapter(getApplicationContext(),dairy,price,image);
                r_v_Dairy.setAdapter(recyclerAdapter);
                r_v_Dairy.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



        DatabaseReference reference5 = instance.getReference("Packed Goods");
        reference5.addValueEventListener(new ValueEventListener() {
            String packed[],image[];
            int price[];
            ArrayList<String> bf = new ArrayList<>();
            ArrayList<Integer> price_a = new ArrayList<>();
            ArrayList<String> image_a = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                bf = (ArrayList<String>) snapshot.child("Name").getValue();
                image_a = (ArrayList<String>) snapshot.child("Image").getValue();
                price_a = (ArrayList<Integer>) snapshot.child("Price").getValue();

                packed = new String[bf.size()];
                price = new int[price_a.size()];
                image = new String[image_a.size() ];
                Toast.makeText(HomeActivity.this, ""+price_a, Toast.LENGTH_SHORT).show();

                for (int i=0;i<bf.size();i++)
                {

                    packed[i] = bf.get(i);
                    price[i]= Integer.parseInt(String.valueOf(price_a.get(i)));
                    image[i]= image_a.get(i);
                }

                Toast.makeText(HomeActivity.this, ""+packed, Toast.LENGTH_SHORT).show();
                recyclerAdapter = new RecyclerAdapter(getApplicationContext(),packed,price,image);
                r_v_Packed.setAdapter(recyclerAdapter);
                r_v_Packed.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        DatabaseReference reference4 = instance.getReference("Drinks");
        reference4.addValueEventListener(new ValueEventListener() {
            String drink[],image[];
            int price[];
            ArrayList<String> bf = new ArrayList<>();
            ArrayList<Integer> price_a = new ArrayList<>();
            ArrayList<String> image_a = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                bf = (ArrayList<String>) snapshot.child("Name").getValue();
                image_a = (ArrayList<String>) snapshot.child("Image").getValue();
                price_a = (ArrayList<Integer>) snapshot.child("Price").getValue();

                drink = new String[bf.size()];
                price = new int[price_a.size()];
                image = new String[image_a.size() ];
                Toast.makeText(HomeActivity.this, ""+price_a, Toast.LENGTH_SHORT).show();

                for (int i=0;i<bf.size();i++)
                {

                    drink[i] = bf.get(i);
                    price[i]= Integer.parseInt(String.valueOf(price_a.get(i)));
                    image[i]= image_a.get(i);
                }

                Toast.makeText(HomeActivity.this, ""+drink, Toast.LENGTH_SHORT).show();
                recyclerAdapter = new RecyclerAdapter(getApplicationContext(),drink,price,image);
                r_v_Drinks.setAdapter(recyclerAdapter);
                r_v_Drinks.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



        DatabaseReference reference3 = instance.getReference("Ice Cream");
        reference3.addValueEventListener(new ValueEventListener() {
            String ice[],image[];
            int price[];
            ArrayList<String> bf = new ArrayList<>();
            ArrayList<Integer> price_a = new ArrayList<>();
            ArrayList<String> image_a = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                bf = (ArrayList<String>) snapshot.child("Name").getValue();
                image_a = (ArrayList<String>) snapshot.child("Image").getValue();
                price_a = (ArrayList<Integer>) snapshot.child("Price").getValue();

                ice = new String[bf.size()];
                price = new int[price_a.size()];
                image = new String[image_a.size() ];
                Toast.makeText(HomeActivity.this, ""+price_a, Toast.LENGTH_SHORT).show();

                for (int i=0;i<bf.size();i++)
                {

                    ice[i] = bf.get(i);
                    price[i]= Integer.parseInt(String.valueOf(price_a.get(i)));
                    image[i]= image_a.get(i);
                }

                Toast.makeText(HomeActivity.this, ""+ice, Toast.LENGTH_SHORT).show();
                recyclerAdapter = new RecyclerAdapter(getApplicationContext(),ice,price,image);
                r_v_IceCream.setAdapter(recyclerAdapter);
                r_v_IceCream.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



        BottomNavigationView my_navigator;
        my_navigator = findViewById(R.id.my_navigator);
        int account = R.id.account,home = R.id.home,menu = R.id.menu,cart = R.id.cart,history = R.id.history;
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
                } else if (item.getItemId() == history) {
                    Intent intent = new Intent(getApplicationContext(),HistoryActivity.class);
                    startActivity(intent);
                    finish();

                }


                return true;
            }
        });

    }


    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Finish the activity and exit the app
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Dismiss the dialog and continue
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        // This line is important to call the default behavior (finishing the current activity)
    }


    @Override
    protected void onStart() {
        super.onStart();
        startAutoScroll();
    }
    private void startAutoScroll() {

        handler.postDelayed(autoScrollRunnable, 2000); // Start the auto-scrolling
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopAutoScroll();
    }

    private void stopAutoScroll() {
        handler.removeCallbacks(autoScrollRunnable); // Stop the auto-scrolling
    }





}