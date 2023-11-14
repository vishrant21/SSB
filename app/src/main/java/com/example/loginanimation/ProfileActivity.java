package com.example.loginanimation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

    ImageView imgProfile;

    ActivityResultLauncher<Intent> activityResultLauncher;
    private EditText editTxtName, editTxtEmail,editTxtAddress,editTxtPhone;
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
        imgProfile = findViewById(R.id.imgProfile);
        editTxtAddress = findViewById(R.id.editTxtAddress);
        editTxtPhone = findViewById(R.id.editTxtPhone);
        txtAddress = findViewById(R.id.txtAddress);
        lytInfo = findViewById(R.id.lytInfo);
        editTxtName = findViewById(R.id.editTxtName);
        editTxtEmail = findViewById(R.id.editTxtEmail);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnLogout = findViewById(R.id.btnLogout);
        txtPhone = findViewById(R.id.txtPhone);


        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                        builder.setTitle("Choose Image Source");
                        builder.setItems(new CharSequence[]{"Camera", "Gallery"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    // User chose the Camera option
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    activityResultLauncher.launch(intent);
                                } else if (which == 1) {
                                    // User chose the Gallery option
                                    Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(intent1, 3);
                                }
                            }
                        });
                        builder.show();
            }
        });

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() !=null)
                        {
                            Bitmap bp = null;
                            Bundle extras = result.getData().getExtras();
                            if (extras != null)
                            {
                                bp = (Bitmap) extras.get("data");
                            }

                            imgProfile.setImageBitmap(bp);
                        } else if (result.getResultCode() == RESULT_CANCELED) {
                            Toast.makeText(ProfileActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


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
            editTxtAddress.setVisibility(View.VISIBLE);
            editTxtPhone.setVisibility(View.VISIBLE);

            // Set edit text values
            editTxtName.setText(txtPname.getText().toString());
            editTxtEmail.setText(txtEmail.getText().toString());
            editTxtPhone.setText("+1 ");

            // Change button text
            btnEditProfile.setText("Save");
        } else {
            // Save changes and switch back to view mode
            txtPname.setText(editTxtName.getText().toString());
            txtEmail.setText(editTxtEmail.getText().toString());
            txtAddress.setText(editTxtAddress.getText().toString().trim());
            txtPhone.setText(editTxtPhone.getText().toString().trim());

            // Switch back to view mode
            lytInfo.setVisibility(View.VISIBLE);
            editTxtName.setVisibility(View.GONE);
            editTxtEmail.setVisibility(View.GONE);
            editTxtAddress.setVisibility(View.GONE);
            editTxtPhone.setVisibility(View.GONE);

            // Change button text
            btnEditProfile.setText("Edit Profile");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null){
            Uri selectedImage = data.getData();
            Toast.makeText(this, ""+data.getData(), Toast.LENGTH_SHORT).show();
            imgProfile.setImageURI(selectedImage);
        }
    }
}