package com.example.loginanimation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class RecieptActivity extends AppCompatActivity {

    Intent intent;
    ListView listView;
    String list[];
    int emailOrder;
    TextView txtOrderNum;
    DatabaseReference myRef;
    SharedPreferences sharedPreferences2;
    int index =0;
    ArrayList<String> storedEmail = new ArrayList<>();
    Calendar calendar = Calendar.getInstance();
    double total;
    TextView txtTotal,txtDate;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reciept);
        listView = findViewById(R.id.lstReciept);
        txtDate = findViewById(R.id.txtDate);
        txtTotal = findViewById(R.id.txtRTotal);
        txtOrderNum = findViewById(R.id.txtOrderNumber);

        intent = getIntent();


        listView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        listView.setScrollBarSize(100);


        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM/dd/yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a", Locale.getDefault());

        Random random = new Random();

        // Generate 9 random digits
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            int randomDigit = random.nextInt(10); // Generates a random digit (0 to 9)
            sb.append(randomDigit);
        }

        int orderId =Integer.parseInt(sb.toString());

        txtOrderNum.setText("#"+orderId);


        String formattedDate = dateFormat.format(calendar.getTime());


        String formattedTime = timeFormat.format(calendar.getTime());

// Set the text of the TextView with the formatted date and time
        txtDate.setText("Ordered On: " + formattedDate + " At " + formattedTime);


        list = intent.getStringArrayExtra("Items");
        total = intent.getDoubleExtra("itemPrice",0.0);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        txtTotal.setText("Total Amount Paid: "+total+" ₹");

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        emailOrder = sharedPreferences.getInt("Index",0);

        myRef = database.getReference().child("Orders").child(emailOrder+""+orderId);
        myRef.child("Orders").child(""+orderId).child("Total").setValue(total);
        myRef.setValue(Arrays.asList(list));

        sharedPreferences2 = getSharedPreferences("MyCart", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences2.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent1 = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent1);
        finish();
    }
}