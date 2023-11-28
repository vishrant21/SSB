package com.example.loginanimation;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NewOrderActivity extends AppCompatActivity {

    ArrayList<String> storedEmail = new ArrayList<>();
    ArrayList<String> orderNum = new ArrayList<>();
    ArrayList<String> orderFrom = new ArrayList<>();
    ArrayList<Integer> firstDigit = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    RecyclerView recyclerView;
    String index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);


        firebaseDatabase = FirebaseDatabase.getInstance();
        // Initialize RecyclerView
        recyclerView = findViewById(R.id.r_v_new_order);

        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Orders");

        // Add a ValueEventListener to retrieve data from the "Orders" node
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                // Iterate through all children of the "Orders" node
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    String orderNumber = orderSnapshot.getKey();
                    long x = dataSnapshot.getChildrenCount();
                    if (!orderNumber.isEmpty()) {
                        orderNum.add(orderNumber);
                        firstDigit.add(Integer.parseInt(""+orderNumber.charAt(0)));

                        // Use the first digit as an index to retrieve the email from the "Users" node
                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
                        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                // Retrieve the email
                                storedEmail = (ArrayList<String>) userSnapshot.child("Email").getValue();
                                // Do something with the email
                                showNewOrderNotification(orderNum.size() - 1);
                                NewOrderAdapter adapter = new NewOrderAdapter(getApplicationContext(),orderNum,firstDigit,storedEmail);
                                recyclerView.setAdapter(adapter);
                                Toast.makeText(NewOrderActivity.this, ""+storedEmail, Toast.LENGTH_SHORT).show();
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle potential errors
                                Log.w("YourActivity", "Failed to read value.", error.toException());
                            }
                        });
                    }

                    Toast.makeText(NewOrderActivity.this, ""+orderNumber, Toast.LENGTH_SHORT).show();
                    // Do something with the order data
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors
                Log.w("YourActivity", "Failed to read value.", databaseError.toException());
            }
        });


    }
    private void showNewOrderNotification(int i) {
    }
}
