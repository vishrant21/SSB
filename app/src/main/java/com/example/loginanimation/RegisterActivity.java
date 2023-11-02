package com.example.loginanimation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private ImageView profileImage;
    LottieAnimationView animationView1,animationView2;
    private EditText nameEditText, emailEditText, addressEditText, phoneEditText, usernameEditText, passwordEditText;
    private TextView registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        profileImage = findViewById(R.id.profileImage);
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        addressEditText = findViewById(R.id.addressEditText);
        animationView1 = findViewById(R.id.animationView);
        animationView2 = findViewById(R.id.animationView2);
        phoneEditText = findViewById(R.id.phoneEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);


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
                if (validateRegistration() && isPasswordValid(passwordEditText)) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private boolean validateRegistration() {
        boolean isValid = true;

        // Name Validation
        String name = nameEditText.getText().toString().trim();
        if (name.isEmpty()) {
            nameEditText.setError("Name is required");
            nameEditText.requestFocus();
            isValid = false;
        }

        // Email Validation
        String email = emailEditText.getText().toString().trim();
        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Enter a valid email address");
            emailEditText.requestFocus();
            isValid = false;
        }

        // Address Validation
        String address = addressEditText.getText().toString().trim();
        if (address.isEmpty()) {
            addressEditText.setError("Address is required");
            addressEditText.requestFocus();
            isValid = false;
        }

        // Phone Validation
        String phone = phoneEditText.getText().toString().trim();
        if (phone.isEmpty()) {
            phoneEditText.setError("Phone number is required");
            phoneEditText.requestFocus();
            isValid = false;
        } else if (phone.length() != 10) {
            phoneEditText.setError("Phone number should be 10 digits");
            phoneEditText.requestFocus();
            isValid = false;
        }

        // Username Validation
        String username = usernameEditText.getText().toString().trim();
        if (username.isEmpty()) {
            usernameEditText.setError("Username is required");
            usernameEditText.requestFocus();
            isValid = false;
        }

        return isValid;
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
}