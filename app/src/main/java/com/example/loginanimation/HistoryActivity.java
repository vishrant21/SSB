package com.example.loginanimation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView r_v_history;
    int index;
    ArrayList<String> uniqueOrderNums = new ArrayList<>();
    ArrayList<String> orderNum = new ArrayList<>();
    SharedPreferences sharedPreferences;
    ArrayList<Integer> firstDigit = new ArrayList<>();
    ArrayList<Integer> uniqueFirstDigit = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        r_v_history = findViewById(R.id.r_v_history);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        index = sharedPreferences.getInt("Index",-1);
        Toast.makeText(this, ""+index, Toast.LENGTH_SHORT).show();

        if (sharedPreferences.getBoolean("isAdminLoggedIn",false)){
            DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Orders");

            // Add a ValueEventListener to retrieve data from the "Orders" node
            ordersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                        String orderNumber = orderSnapshot.getKey();
                        if (!orderNumber.isEmpty()) {
                            orderNum.add(orderNumber);
                            firstDigit.add(Integer.parseInt("" + orderNumber.charAt(0)));
                            for (int i = 0; i < orderNum.size(); i++) {
                                if (Integer.parseInt(orderNum.get(i).charAt(0)+"") == index) {
                                    // Display the ordered items in the ListVie
                                    uniqueOrderNums.add(orderNum.get(i));

                                }
                            }
//                                showNotification("New Order" ,"Click to see Items");
//                                NewOrderAdapter adapter = new NewOrderAdapter(getApplicationContext(), uniqueOrderNums , uniqueFirstDigit, storedEmail);
                            HistoryAdapter historyAdapter = new HistoryAdapter(getApplicationContext(), orderNum);
                            r_v_history.setAdapter(historyAdapter);
                            r_v_history.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle potential errors
                    Log.w("YourActivity", "Failed to read value.", databaseError.toException());
                }
            });

        }
        else {
            DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Orders");

            // Add a ValueEventListener to retrieve data from the "Orders" node
            ordersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                        String orderNumber = orderSnapshot.getKey();
                        if (!orderNumber.isEmpty()) {
                            orderNum.add(orderNumber);
                            firstDigit.add(Integer.parseInt("" + orderNumber.charAt(0)));
                            for (int i = 0; i < orderNum.size(); i++) {
                                if (Integer.parseInt(orderNum.get(i).charAt(0) + "") == index) {
                                    // Display the ordered items in the ListVie
                                    uniqueOrderNums.add(orderNum.get(i));

                                }
                            }
//                                showNotification("New Order" ,"Click to see Items");
//                                NewOrderAdapter adapter = new NewOrderAdapter(getApplicationContext(), uniqueOrderNums , uniqueFirstDigit, storedEmail);
                            HistoryAdapter historyAdapter = new HistoryAdapter(getApplicationContext(), uniqueOrderNums);
                            r_v_history.setAdapter(historyAdapter);
                            r_v_history.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle potential errors
                    Log.w("YourActivity", "Failed to read value.", databaseError.toException());
                }
            });
        }
        BottomNavigationView my_navigator;
        my_navigator = findViewById(R.id.my_navigator);
        int account = R.id.account,home = R.id.home,menu = R.id.menu,cart = R.id.cart,history = R.id.history;
        my_navigator.setSelectedItemId(R.id.history);
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
                } else if (item.getItemId() == home) {
                    Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                    startActivity(intent);
                    finish();

                }


                return true;
            }
        });


    }
}