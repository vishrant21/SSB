package com.example.loginanimation;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NewOrderActivity extends AppCompatActivity {

    ArrayList<String> storedEmail = new ArrayList<>();
    ArrayList<String> orderNum = new ArrayList<>();
    ArrayList<String> orderFrom = new ArrayList<>();
    private static final int PERMISSION_REQUEST_CODE = 123;

    NotificationManagerCompat notificationManager;
    ArrayList<Integer> firstDigit = new ArrayList<>();
    private static final String CHANNEL_ID = "MyChannelID";
    FirebaseDatabase firebaseDatabase;
    RecyclerView recyclerView;
    String index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);


        firebaseDatabase = FirebaseDatabase.getInstance();
        // Initialize RecyclerView
        recyclerView = findViewById(R.id.r_v_new_order);

        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Orders");

        // Add a ValueEventListener to retrieve data from the "Orders" node
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                // Iterate through all children of the "Orders" node
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    String orderNumber = orderSnapshot.getKey();
                    long x = dataSnapshot.getChildrenCount();
                    if (!orderNumber.isEmpty()) {
                        orderNum.add(orderNumber);
                        firstDigit.add(Integer.parseInt("" + orderNumber.charAt(0)));

                        // Use the first digit as an index to retrieve the email from the "Users" node
                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
                        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                // Retrieve the email
                                notificationManager = NotificationManagerCompat.from(NewOrderActivity.this);
                                storedEmail = (ArrayList<String>) userSnapshot.child("Email").getValue();
                                // Do something with the email
                                showNotification("New Order" ,"Order Number : " +(orderNum.get(orderNum.size() - 1)));
                                NewOrderAdapter adapter = new NewOrderAdapter(getApplicationContext(), orderNum, firstDigit, storedEmail);
                                recyclerView.setAdapter(adapter);
                                Toast.makeText(NewOrderActivity.this, "" + storedEmail, Toast.LENGTH_SHORT).show();
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle potential errors
                                Log.w("YourActivity", "Failed to read value.", error.toException());
                            }
                        });
                    }

                    Toast.makeText(NewOrderActivity.this, "" + orderNumber, Toast.LENGTH_SHORT).show();
                    // Do something with the order data
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors
                Log.w("YourActivity", "Failed to read value.", databaseError.toException());
            }
        });



    }



    // Add this constant at the beginning of your class
    // You can use any value

    // Override onRequestPermissionsResult to handle the result of the permission request
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            // Check if the permission was granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, show the notification again
                showNotification("Data Uploaded", "Your data has been successfully uploaded to the database.");
            } else {
                // Permission denied, handle accordingly (e.g., show a message to the user)
                Toast.makeText(this, "Permission denied. Notification cannot be shown.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createNotificationChannel () {
        // Create the NotificationChannel, but only on API 26+ because the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MyChannel";
            String description = "My Notification Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void showNotification(String title, String message) {
        createNotificationChannel();
        // Check if the app has the POST_NOTIFICATIONS permission
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Request the permission
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_CODE);
            return;
        }

        // Continue with building and displaying the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.my_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        // Create an explicit intent for an activity in your app
        Intent intent = new Intent(this, MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);

        builder.setContentIntent(pendingIntent);

        // Automatically removes the notification when the user taps it
        builder.setAutoCancel(true);

        // Build the notification and display it
        notificationManager.notify(1, builder.build()); // Use a unique ID (1 in this case) for each notification
    }
}