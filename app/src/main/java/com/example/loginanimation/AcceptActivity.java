package com.example.loginanimation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class AcceptActivity extends AppCompatActivity {

    TextView txtOrderNum,txtOtotal;
    ListView lstOrder;
    String orderNum;
    List<String> orderedItems = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept);
        txtOrderNum = findViewById(R.id.txtONumber);
        lstOrder  = findViewById(R.id.lstOrder);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Orders");
        Intent intent = getIntent();
        orderNum = intent.getStringExtra("OrderNum");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderedItems = (ArrayList<String>) snapshot.child(""+orderNum).getValue();
                String[] array = new String[orderedItems.size()];
                orderedItems.toArray(array);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,array);
                lstOrder.setAdapter(adapter);
                txtOrderNum.setText("#"+orderNum);
//                String total = snapshot.child(""+orderNum).child("Total").getValue().toString();
//                txtOtotal.setText("Total Amount : "+total+" ₹");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}