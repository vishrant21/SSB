package com.example.loginanimation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    LottieAnimationView animationView1, animationView2,animationView3, animationView4;
    ActivityResultLauncher<Intent> activityResultLauncher;

    LottieAnimationView viewAnime;
    ArrayList<String> userName = new ArrayList<>();
    ImageView profileimage;
    private EditText nameEditText, usernameEditText, passwordEditText,phoneEditText,addressEditText;
    private TextView registerButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEditText = findViewById(R.id.nameEditText);


        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        profileimage = findViewById(R.id.profileImageReg);
        animationView1 = findViewById(R.id.animationView);
        animationView2 = findViewById(R.id.animationView2);
        nameEditText = findViewById(R.id.nameEditText);
        animationView3 = findViewById(R.id.animationView3);
        animationView4 = findViewById(R.id.animationView4);
        phoneEditText = findViewById(R.id.phoneEditText);
        addressEditText = findViewById(R.id.addressEditText);
        viewAnime = findViewById(R.id.viewAnime);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);


        DatabaseReference reference = instance.getReference("Users");
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userName = (ArrayList<String>) snapshot.child("Email").getValue();

                        String myUname = usernameEditText.getText().toString().trim();
                        if (userName.contains(myUname) || (!isEmailValid(myUname)))
                        {
                            animationView3.setVisibility(View.GONE);
                            animationView4.setVisibility(View.VISIBLE);
                            animationView4.playAnimation();


                        } else if ((!userName.contains(myUname)) && (isEmailValid(myUname)))
                        {
                            animationView4.setVisibility(View.GONE);
                            animationView3.setVisibility(View.VISIBLE);
                            animationView3.playAnimation();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isPasswordValid(passwordEditText))
                {
                    animationView2.setVisibility(View.GONE);
                    animationView1.setVisibility(View.VISIBLE);
                    animationView1.playAnimation();


                } else if (!isPasswordValid(passwordEditText))
                {
                    animationView1.setVisibility(View.GONE);
                    animationView2.setVisibility(View.VISIBLE);
                    animationView2.playAnimation();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isPasswordValid(passwordEditText) && isEmailValid(usernameEditText.getText().toString().trim()))
                {
                    if (!nameEditText.getText().toString().equals(null) && !addressEditText.getText().toString().equals(null) && !phoneEditText.getText().toString().equals(null))
                    {
                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);

                        Map<String,Object> al_name = new HashMap<>();
                        Map<String,Object> al_email = new HashMap<>();
                        Map<String,Object> al_pass = new HashMap<>();
                        Map<String,Object> al_phone = new HashMap<>();
                        Map<String,Object> al_address = new HashMap<>();
                        al_name.put(userName.size()+"",nameEditText.getText().toString().trim());
                        al_email.put(userName.size()+"",usernameEditText.getText().toString().trim());
                        al_pass.put(userName.size()+"",passwordEditText.getText().toString().trim());
                        al_pass.put(userName.size()+"",phoneEditText.getText().toString().trim());
                        al_pass.put(userName.size()+"",addressEditText.getText().toString().trim());
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                DatabaseReference nameRef = reference.child("Name");
                                nameRef.updateChildren(al_name);
                                DatabaseReference emailRef = reference.child("Email");
                                emailRef.updateChildren(al_email);
                                DatabaseReference passRef = reference.child("Password");
                                passRef.updateChildren(al_pass);
                                DatabaseReference phoneRef = reference.child("Phone");
                                phoneRef.updateChildren(al_phone);
                                DatabaseReference addRef = reference.child("Address");
                                addRef.updateChildren(al_address);



                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        viewAnime.setVisibility(View.VISIBLE);
                        viewAnime.playAnimation();
                        new Handler().postDelayed(new Runnable() {
                            @Override

                            public void run() {
                                startActivity(intent);
                                finish();
                            }
                        },viewAnime.getDuration());

                    }
                    else
                    {
                        nameEditText.setError("Please enter Name First");
                        phoneEditText.setError("Please enter Phone First");
                        addressEditText.setError("Please enter Address First");
                    }
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "Please enter valid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null){
            Uri selectedImage = data.getData();
            Toast.makeText(this, ""+data.getData(), Toast.LENGTH_SHORT).show();
            profileimage.setImageURI(selectedImage);
        }
    }

    private boolean isPasswordValid(EditText passwordEditText) {
        String password = passwordEditText.getText().toString().trim();
        String regex = "^(?=(.*\\d){3,})(?=(.*\\W){1,}).{8,}$"; // At least 3 numbers, 1 symbol, and 8 characters

        if (!Pattern.matches(regex, password)) {
            Toast.makeText(this, "Password should have atleaast 3 numbers 1 symbol and 8 Characters", Toast.LENGTH_SHORT).show();
            passwordEditText.requestFocus();
            return false;
        }
        return true;
    }

    public boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}