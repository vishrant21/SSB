package com.example.loginanimation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class RecieptActivity extends AppCompatActivity {

    Intent intent;
    ListView listView;
    String list[];
    String emailOrder;
    int x  = 0,index =0;
    ArrayList<String> storedEmail = new ArrayList<>();
    Calendar calendar = Calendar.getInstance();
    double total;
    TextView txtTotal,txtDate;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reciept);
        listView = findViewById(R.id.lstReciept);
        txtDate = findViewById(R.id.txtDate);
        txtTotal = findViewById(R.id.txtRTotal);
        intent = getIntent();


        listView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        listView.setScrollBarSize(100);


        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM/dd/yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a", Locale.getDefault());


        String formattedDate = dateFormat.format(calendar.getTime());


        String formattedTime = timeFormat.format(calendar.getTime());

// Set the text of the TextView with the formatted date and time
        txtDate.setText("Ordered On: " + formattedDate + " At " + formattedTime);


        list = intent.getStringArrayExtra("Items");
        total = intent.getDoubleExtra("itemPrice",0.0);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        txtTotal.setText("Total Amount Paid: "+total+" ₹");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child("Email");

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        if (sharedPreferences.getBoolean("isAdminLoggedIn",false))
        {
//            Toast.makeText(this, "Admin Login "+sharedPreferences.getBoolean("isAdminLoggedIn",false), Toast.LENGTH_SHORT).show();
            index = 0;
        }
        else
        {
            index = sharedPreferences.getInt("Index",0);
        }

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                storedEmail = (ArrayList<String>) snapshot.getValue();

                emailOrder = storedEmail.get(index);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        x++;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Orders").child(""+emailOrder).child(""+x);
        myRef.setValue(Arrays.asList(list));

    }
}