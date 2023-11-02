package com.example.loginanimation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    LottieAnimationView animationView1,animationView2;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ImageView profileimage;
    private EditText nameEditText, emailEditText, addressEditText, phoneEditText, usernameEditText, passwordEditText;
    private TextView registerButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEditText = findViewById(R.id.nameEditText);
        profileimage = findViewById(R.id.profileImageReg);
        emailEditText = findViewById(R.id.emailEditText);
        addressEditText = findViewById(R.id.addressEditText);
        animationView1 = findViewById(R.id.animationView);
        animationView2 = findViewById(R.id.animationView2);
        phoneEditText = findViewById(R.id.phoneEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);


        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
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

                            profileimage.setImageBitmap(bp);
                        } else if (result.getResultCode() == RESULT_CANCELED) {
                            Toast.makeText(RegisterActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                        }
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
}