package com.example.loginanimation;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddNewItemActivity extends AppCompatActivity {
    private ImageView imgNewItem;
    private EditText txtNewName;
    private EditText txtNewPrice;
    TextView txtMenuitem;
    Map<String, Object> al_image = new HashMap<>();
    Map<String, Object> al_name = new HashMap<>();
    Map<String, Object> al_price = new HashMap<>();

    private NotificationManagerCompat notificationManager;
    private static final String CHANNEL_ID = "MyChannelID";
    Uri selectedImageUri, recent_uri;

    DatabaseReference reference;
    String category;
    ArrayList<String> username = new ArrayList<>();
    FirebaseDatabase instance = FirebaseDatabase.getInstance();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    ActivityResultLauncher<Intent> activityResultLauncher;
    private Button addData;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);

        // Initialize the views
        txtMenuitem = findViewById(R.id.txtMenuitem);
        imgNewItem = findViewById(R.id.imgNewItem);
        txtNewName = findViewById(R.id.txtNewName);
        txtNewPrice = findViewById(R.id.txtNewPrice);
        addData = findViewById(R.id.addData);
        category = getIntent().getStringExtra("Category");
        txtMenuitem.setText("" + category);

        // Now you can use these views in your Java code
        // For example, you can set a click listener for the button
        DatabaseReference reference1 = instance.getReference(category);

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username = (ArrayList<String>) snapshot.child("Name").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imgNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // User chose the Gallery option
                Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intent1);
            }
        });

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            if (result.getData().getData() != null) {
                                // User selected an image from the gallery
                                selectedImageUri = result.getData().getData();
                                try {
                                    Bitmap bp = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                                    imgNewItem.setImageURI(selectedImageUri);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                // Camera result, handle it as before
                                Bundle extras = result.getData().getExtras();
                                if (extras != null) {
                                    Bitmap bp = (Bitmap) extras.get("data");
                                    imgNewItem.setImageURI(selectedImageUri);
                                }
                            }
                        } else if (result.getResultCode() == RESULT_CANCELED) {
                            Toast.makeText(AddNewItemActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get data from views
                notificationManager = NotificationManagerCompat.from(AddNewItemActivity.this);
                String itemName = txtNewName.getText().toString().trim();
                String itemPrice = txtNewPrice.getText().toString().trim();

                // Check if the data is not empty
                if (!itemName.isEmpty() && !itemPrice.isEmpty()) {
                    // Get the category from the intent


                    // Update the database
                    updateFirebaseDatabase(category, itemName, itemPrice);
                    showNotification("Data Uploaded", "Your data has been successfully uploaded to the database.");
                    Toast.makeText(AddNewItemActivity.this, "Item Added to Database", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddNewItemActivity.this, MenuActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(AddNewItemActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // ... (rest of your code)
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

    // Add this constant at the beginning of your class
    private static final int PERMISSION_REQUEST_CODE = 123; // You can use any value

    // Override onRequestPermissionsResult to handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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

    private void createNotificationChannel() {
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

    // Method to update Firebase database
    private void updateFirebaseDatabase(String category, String itemName, String itemPrice) {
        // Create a reference to the category node
        reference = instance.getReference(category);
        
        al_name.put(username.size()+"",txtNewName.getText().toString().trim());
        al_price.put(username.size()+"",Integer.parseInt(txtNewPrice.getText().toString().trim()));
        if (selectedImageUri != null)
        {
           uploadToFirebaseStorage(selectedImageUri, username.size());
        }
        else{
            Toast.makeText(this, "Image is must please select an image", Toast.LENGTH_SHORT).show();
        }



    }

    private void uploadToFirebaseStorage(Uri uri, int size) {
        final StorageReference fileRef = FirebaseStorage.getInstance().getReference().child(System.currentTimeMillis() + "." + getFileExtension(uri));

        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // TaskSnapshot provides information about the uploaded file
                // Now get the download URL
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUri) {
                        // Now you have the download URL
                        // Use it as needed, for example, store it in the Realtime Database
                        String imageUrl = downloadUri.toString();
                        al_image.put("" + size, imageUrl);
                        DatabaseReference passRef = reference.child("Image");
                        passRef.updateChildren(al_image);
                        DatabaseReference nameRef = reference.child("Name");
                        nameRef.updateChildren(al_name);
                        DatabaseReference emailRef = reference.child("Price");
                        emailRef.updateChildren(al_price);
//                        Toast.makeText(AddNewItemActivity.this, "Uploaded Successfully"+al_image, Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the failure to get the download URL
                        Toast.makeText(AddNewItemActivity.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Upload", "Failed to upload file", e);
                Toast.makeText(AddNewItemActivity.this, ""+e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri muri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(muri));
    }


}