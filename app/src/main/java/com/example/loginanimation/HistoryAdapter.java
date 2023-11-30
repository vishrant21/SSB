// HistoryAdapter.java
package com.example.loginanimation;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.OrderViewHolder> {

    private Context context;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Orders");
    ArrayList<String> orderNum = new ArrayList<>();
    private Handler uiHandler = new Handler();
    ArrayList<String> orderedItems = new ArrayList<>();

    public HistoryAdapter(Context context, ArrayList<String> orderNum) {
        this.context = context;
        this.orderNum = orderNum;

    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.r_v_item_history, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.lstOrder.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        holder.lstOrder.setScrollBarSize(10);
        holder.txtOrderNum.setText("Order: #" + orderNum.get(position));

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderedItems = (ArrayList<String>) snapshot.child("" + orderNum.get(position)).getValue();
                // Display the ordered items in the ListView
                String[] array = new String[orderedItems.size()];
                orderedItems.toArray(array);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, array);
                holder.lstOrder.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return orderNum.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderNum;
        LinearLayout lyt;
        ListView lstOrder;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderNum = itemView.findViewById(R.id.txtOrderNumHistory);
            lyt = itemView.findViewById(R.id.lyt);
            lstOrder = itemView.findViewById(R.id.lstHistory);
        }
    }
}
