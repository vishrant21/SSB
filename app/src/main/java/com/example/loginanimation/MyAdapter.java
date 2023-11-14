package com.example.loginanimation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    ArrayList<String> c_names;
    ArrayList<String> c_image;
    ArrayList<Integer> c_price;
    ArrayList<Integer> c_qty;
    CartActivity cartActivity;
    Context context;

    public MyAdapter(CartActivity cartActivity,Context context, ArrayList<String> c_names, ArrayList<Integer> c_price, ArrayList<Integer> c_qty, ArrayList<String> c_image) {

        this.cartActivity = cartActivity;
        this.context = context;
        this.c_names = c_names;
        this.c_price = c_price;
        this.c_qty = c_qty;
        this.c_image = c_image;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.r_v_cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txtCprice.setText(c_price.get(position)+" ₹");
        holder.txtCname.setText(""+c_names.get(position));
        holder.txtCqty.setText(" X "+c_qty.get(position));
        Picasso.get().load(c_image.get(position)).into(holder.imgCitem);

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    cartActivity.whenSwiped(position);
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ItemActivity.class);
                intent.putExtra("edit",1);
                    intent.putExtra("posImage",c_image.get(position));
                    intent.putExtra("position",position);
                    intent.putExtra("posPrice",c_price.get(position));
                    intent.putExtra("posQty",c_qty.get(position));
                    intent.putExtra("posName",c_names.get(position));
                    Toast.makeText(context, "Adapter :"+c_image.size(), Toast.LENGTH_LONG).show();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return c_names.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtCname,txtCprice,txtCqty;
        public ImageView imgCitem,btnEdit,btnDelete;


        public ViewHolder(View view) {
            super(view);
            txtCname = view.findViewById(R.id.txtCname);
            txtCprice = view.findViewById(R.id.txtCprice);
            btnEdit = view.findViewById(R.id.btnEdit);
            imgCitem = view.findViewById(R.id.imgCitem);
            btnDelete = view.findViewById(R.id.btnDelete);
            txtCqty = view.findViewById(R.id.txtCqty);
        }
    }
    public interface ActivityFinishListener {
        void finishActivity();
    }
}

