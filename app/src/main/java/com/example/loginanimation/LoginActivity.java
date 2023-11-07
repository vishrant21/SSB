package com.example.loginanimation;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    LinearLayout loginLayout;
    ImageView logoImageView;
    TextView btnRegister;
    CardView toolCard;
    Button btnAdmin;
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
        my_toolbar = findViewById(R.id.my_toolbar);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin = findViewById(R.id.LoginButton);
        Animation fade;
        fade = AnimationUtils.loadAnimation(this,R.anim.fade_out);

        loginLayout.startAnimation(fade);

        my_toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
                    public boolean onMenuItemClick(MenuItem item) {


                Dialog smallLayoutDialog = new Dialog(LoginActivity.this);

                // Set the content view of the dialog to your small layout
                smallLayoutDialog.setContentView(R.layout.dialogue_alert);

                btnAdmin = smallLayoutDialog.findViewById(R.id.btnAdmin);
                smallLayoutDialog.show();
                btnAdmin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(LoginActivity.this, "Admin Pressed", Toast.LENGTH_SHORT).show();
                        smallLayoutDialog.dismiss();
                    }
                });
                        return true;
                    }
                });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);

                animationView.setVisibility(View.VISIBLE);
                animationView.playAnimation();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                        finish();
                    }
                },animationView.getDuration()+300);

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
}