// NewOrderAdapter.java
package com.example.loginanimation;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NewOrderAdapter extends RecyclerView.Adapter<NewOrderAdapter.OrderViewHolder> {

    private Context context;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Orders");
    ArrayList<String> orderNum = new ArrayList<>();
    CountDownTimer countDownTimer;
    private Handler uiHandler = new Handler();
    ArrayList<String> orderedItems = new ArrayList<>();
    ArrayList<String> uniqueOrderNums = new ArrayList<>();
    ArrayList<Integer> firstDigit = new ArrayList<>();
    ArrayList<String> storedEmail = new ArrayList<>();

    public NewOrderAdapter(Context context, ArrayList<String> orderNum, ArrayList<Integer> firstdigit, ArrayList<String> storedEmail) {
        this.context = context;
        this.orderNum = orderNum;
        this.firstDigit = firstdigit;
        this.storedEmail = storedEmail;

    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        int index = firstDigit.get(position);

        holder.lstOrder.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        holder.lstOrder.setScrollBarSize(10);
        String email = storedEmail.get(index);
        holder.txtOrderNum.setText("Order: #" + orderNum.get(position));
        holder.txtFrom.setText("Order from: " + email);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderedItems = (ArrayList<String>) snapshot.child("" + orderNum.get(position)).getValue();
                // Display the ordered items in the ListView
                String[] array = new String[orderedItems.size()];
                uniqueOrderNums.add(orderNum.get(position));
                orderedItems.toArray(array);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, array);
                holder.lstOrder.setAdapter(adapter);

                holder.btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Disable the button to prevent multiple clicks
                        holder.btnAccept.setEnabled(false);

                        // Start the CountDownTimer for 15 minutes
                        startCountDownTimer(15 * 60 * 1000, holder); // 15 minutes in milliseconds
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Disable the button to prevent multiple clicks
                holder.btnAccept.setEnabled(false);

                // Start the CountDownTimer for 15 minutes
                startCountDownTimer(15 * 60 * 1000, holder); // 15 minutes in milliseconds
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderNum.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderNum;
        TextView txtFrom;
        LinearLayout lyt;
        ListView lstOrder;
        ProgressBar progressBar;
        Button btnAccept;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderNum = itemView.findViewById(R.id.txtOrderNum);
            txtFrom = itemView.findViewById(R.id.txtFrom);
            lyt = itemView.findViewById(R.id.lyt);
            progressBar = itemView.findViewById(R.id.progressBar);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            lstOrder = itemView.findViewById(R.id.lstOrder);
        }
    }

    private void updateProgressBar(int progress,OrderViewHolder holder) {
        uiHandler.post(() ->
        {
            holder.progressBar.setMax((int) (15 * 60 * 1000));
            holder.progressBar.setProgress(progress);
        });
    }

    // Handle the desired action when the countdown finishes (e.g., play a sound)
    private void handleCountdownFinish(OrderViewHolder holder) {
        // Add your code to handle the countdown finish, e.g., play a sound
        // For example, you can use MediaPlayer to play a sound here

        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.sound_effext);
        mediaPlayer.start();
        orderNum.remove(holder.getAdapterPosition());
        notifyItemRemoved(holder.getAdapterPosition());
    }

    private void startCountDownTimer(long duration,OrderViewHolder holder) {
        countDownTimer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Update the progress bar
                int progress = (int) (duration - millisUntilFinished);
                updateProgressBar(progress,holder);
            }

            @Override
            public void onFinish() {
                // Countdown finished, handle the desired action (e.g., play a sound)
                handleCountdownFinish(holder);
            }
        };

        countDownTimer.start();
    }
}
