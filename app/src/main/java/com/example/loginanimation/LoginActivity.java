package com.example.loginanimation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
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
        Animation animation = animationView.getAnimation();

        btnLogin = findViewById(R.id.LoginButton);
        Animation fade;
        fade = AnimationUtils.loadAnimation(this,R.anim.fade_out);

        loginLayout.startAnimation(fade);

        my_toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.dialogue_alert, null);

                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setView(dialogView);

                        TextView messageTextView = dialogView.findViewById(R.id.txtAlert);
                        messageTextView.setText("You do not have administration access to use this feature.");
                        builder.setTitle("Administration Access Required !!");

//                        ImageView customImageView = dialogView.findViewById(R.id.customImageView);

//                        customImageView.setImageResource(R.drawable.custom_image);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Handle the positive button click
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Handle the negative button click
                                    }
                                });

                        AlertDialog dialog = builder.create();
                        dialog.show();

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
            }
        });

    }
}