package com.example.loginanimation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    LinearLayout lytInfo;

    private EditText editTxtName, editTxtEmail;
    private TextView btnEditProfile;

    TextView txtPname,txtEmail,txtAddress,txtPhone,btnLogout;
    ArrayList<String> storedEmail = new ArrayList<>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        txtEmail = findViewById(R.id.txtEmail);
        txtPname = findViewById(R.id.txtPname);
        txtAddress = findViewById(R.id.txtAddress);
        lytInfo = findViewById(R.id.lytInfo);
        editTxtName = findViewById(R.id.editTxtName);
        editTxtEmail = findViewById(R.id.editTxtEmail);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnLogout = findViewById(R.id.btnLogout);
        txtPhone = findViewById(R.id.txtPhone);



        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked Yes button
                                // Implement logout functionality here
                                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("isUserLoggedIn", false);
                                editor.apply();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked No button
                                // Dismiss the dialog (no action needed)
                                dialog.dismiss();
                            }
                        });

                // Create and show the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
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
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleEditMode();
            }
        });
    }

    private void toggleEditMode() {

        if (editTxtName.getVisibility() == View.GONE) {
            // Switch to edit mode
            lytInfo.setVisibility(View.GONE);
            editTxtName.setVisibility(View.VISIBLE);
            editTxtEmail.setVisibility(View.VISIBLE);

            // Set edit text values
            editTxtName.setText(txtPname.getText().toString());
            editTxtEmail.setText(txtEmail.getText().toString());

            // Change button text
            btnEditProfile.setText("Save");
        } else {
            // Save changes and switch back to view mode
            txtPname.setText(editTxtName.getText().toString());
            txtEmail.setText(editTxtEmail.getText().toString());

            // Switch back to view mode
            lytInfo.setVisibility(View.VISIBLE);
            editTxtName.setVisibility(View.GONE);
            editTxtEmail.setVisibility(View.GONE);

            // Change button text
            btnEditProfile.setText("Edit Profile");
        }
    }
}