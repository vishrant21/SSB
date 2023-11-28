// NewOrderAdapter.java
package com.example.loginanimation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NewOrderAdapter extends RecyclerView.Adapter<NewOrderAdapter.OrderViewHolder> {

    private Context context;
    ArrayList<String> orderNum = new ArrayList<>();
    ArrayList<Integer> firstDigit = new ArrayList<>();
    ArrayList<String> storedEmail = new ArrayList<>();

    public NewOrderAdapter(Context context, ArrayList<String> orderNum, ArrayList<Integer> firstdigit , ArrayList<String> storedEmail){
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
        holder.txtOrderNum.setText("Order: #" + orderNum.get(position));
        if (index >= 0 && index < storedEmail.size()) {
            String email = storedEmail.get(index);
            holder.txtOrderNum.setText("Order: #" + orderNum.get(position));
            holder.txtFrom.setText("Order from: " + email);

        } else {
            // Handle the case where the index is out of bounds
            holder.txtOrderNum.setText("Invalid Order");
            holder.txtFrom.setText("Invalid Order");
        }
    }

    @Override
    public int getItemCount() {
        return orderNum.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderNum;
        TextView txtFrom;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderNum = itemView.findViewById(R.id.txtOrderNum);
            txtFrom = itemView.findViewById(R.id.txtFrom);
        }
    }
}
