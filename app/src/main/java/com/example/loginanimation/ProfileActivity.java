package com.example.loginanimation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    int index=-1;
    ArrayList<String> storedName = new ArrayList<>();

    TextView txtPname,txtEmail,txtAddress,txtPhone;
    ArrayList<String> storedEmail = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        txtEmail = findViewById(R.id.txtEmail);
        txtPname = findViewById(R.id.txtPname);
        txtAddress = findViewById(R.id.txtAddress);
        txtPhone = findViewById(R.id.txtPhone);


        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        index = sharedPreferences.getInt("Index",-1);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                storedName = (ArrayList<String>) snapshot.child("Name").getValue();
                storedEmail = (ArrayList<String>) snapshot.child("Email").getValue();

                txtEmail.setText(""+storedEmail.get(index));
                txtPname.setText(""+storedName.get(index));

                txtPhone.setText("Not Yet Provided");
                txtAddress.setText("Not yet Provided");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        BottomNavigationView my_navigator;
        my_navigator = findViewById(R.id.my_navigator);
        int account = R.id.account,home = R.id.home,menu = R.id.menu,cart = R.id.cart;
        my_navigator.setSelectedItemId(R.id.account);
        my_navigator.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == menu)
                {
                    Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if (item.getItemId() == cart) {
                    Intent intent = new Intent(getApplicationContext(), CartActivity.class);
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
}