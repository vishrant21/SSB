package com.example.loginanimation;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    LinearLayout loginLayout;
    ImageView logoImageView;
    TextView btnRegister;

    ArrayList<String> storedEmail = new ArrayList<>();
    ArrayList<String> storedPass = new ArrayList<>();
    TextInputEditText usernameEditText,passwordEditText;
    CardView toolCard;
    Button btnAdmin;
    TextInputEditText admin_Uname,admin_pass;
    ScrollView homeScroll;
    androidx.appcompat.widget.Toolbar my_toolbar;
    Button btnLogin;
    LottieAnimationView animationView;

    FirebaseDatabase instance = FirebaseDatabase.getInstance();
    private DatabaseReference snack1;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginLayout = findViewById(R.id.loginLayout);
        toolCard = findViewById(R.id.tool_Card);
        logoImageView = findViewById(R.id.logoImageView);
        animationView = findViewById(R.id.animationView);
        passwordEditText = findViewById(R.id.passwordEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        my_toolbar = findViewById(R.id.my_toolbar);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin = findViewById(R.id.LoginButton);
        Animation fade;

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isUserLoggedIn = sharedPreferences.getBoolean("isUserLoggedIn", false);
        boolean isAdminLoggedIn = sharedPreferences.getBoolean("isAdminLoggedIn", false);

        if (isUserLoggedIn) {
            // User is remembered as logged in, navigate to the main activity

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    animationView.setVisibility(View.GONE);
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish(); // Optionally, close the LoginActivity
                }
            }, animationView.getDuration()-200);

        } else if (isAdminLoggedIn) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    animationView.setVisibility(View.GONE);
                    Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                    startActivity(intent);
                    finish(); // Optionally, close the LoginActivity
                }
            }, animationView.getDuration()-200);


        }
        fade = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        loginLayout.startAnimation(fade);

        my_toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {


                Dialog smallLayoutDialog = new Dialog(LoginActivity.this);

                // Set the content view of the dialog to your small layout
                smallLayoutDialog.setContentView(R.layout.dialogue_alert);

                btnAdmin = smallLayoutDialog.findViewById(R.id.btnAdmin);
                admin_pass = smallLayoutDialog.findViewById(R.id.admin_pass);
                admin_Uname =smallLayoutDialog.findViewById(R.id.admin_Uname);
                smallLayoutDialog.show();
                btnAdmin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (admin_Uname.getText().toString().trim().equals("Sukh Shanti"))
                        {
                            if (admin_pass.getText().toString().trim().equals("Kalol213"))
                            {
                                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                Toast.makeText(LoginActivity.this, "Admin Login", Toast.LENGTH_SHORT).show();
                                smallLayoutDialog.dismiss();
                                editor.putBoolean("isAdminLoggedIn", true);
                                editor.apply();
                                Intent intent1 = new Intent(LoginActivity.this, MenuActivity.class);
                                startActivity(intent1);
                                finish();
                            }
                        }
                        smallLayoutDialog.dismiss();
                    }
                });
                return true;
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String enteredUsername = usernameEditText.getText().toString().trim();
                String enteredPassword = passwordEditText.getText().toString().trim();

                // Check if the fields are not empty
                if (TextUtils.isEmpty(enteredUsername) || TextUtils.isEmpty(enteredPassword)) {
                    Toast.makeText(LoginActivity.this, "Please enter both username and password.", Toast.LENGTH_SHORT).show();
                } else {
                    // Perform authentication by querying the database
                    authenticateUser(enteredUsername, enteredPassword);
                }

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void authenticateUser(String enteredUsername, String enteredPassword) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");


        usersRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                storedEmail = (ArrayList<String>) snapshot.child("Email").getValue();
                storedPass = (ArrayList<String>) snapshot.child("Password").getValue();

                Toast.makeText(LoginActivity.this, ""+storedEmail.get(storedEmail.size()-2), Toast.LENGTH_SHORT).show();


                if (storedEmail.contains(enteredUsername))
                {
                    int x =storedEmail.indexOf(enteredUsername);
                    if(enteredPassword.equals(storedPass.get(x)))
                    {
                        animationView.setVisibility(View.VISIBLE);
                        animationView.playAnimation();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                animationView.setVisibility(View.GONE);
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);

                                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("isUserLoggedIn", true);
                                editor.putString("CurrentUser",enteredUsername);
                                editor.putInt("Index",x);
                                editor.apply();
                                startActivity(intent);
                                finish();
                            }
                        }, animationView.getDuration()-200);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}