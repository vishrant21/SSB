package com.example.loginanimation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private ImageView imgSplash;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgSplash = findViewById(R.id.imgSplash);

        Animation animation;
        animation = AnimationUtils.loadAnimation(this,R.anim.zoom_out);
        imgSplash.setVisibility(View.VISIBLE);
        Animation slide_up;
        slide_up = AnimationUtils.loadAnimation(this,R.anim.slide_up);
        imgSplash.startAnimation(slide_up);

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, 100);
        }
    }