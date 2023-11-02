package com.example.loginanimation;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapterMenu extends RecyclerView.Adapter<RecyclerAdapterMenu.MyViewHolder> {

    String models[];
    int images[];
    Context context;

    public RecyclerAdapterMenu(Context c, String[] model, int[] image){
        models = model;
        images = image;
        context = c;
    }
    @NonNull
    @Override
    public RecyclerAdapterMenu.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.r_v_menu, parent ,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterMenu.MyViewHolder holder, int position) {
        holder.menuTxt.setText(models[position]);
        holder.menuImg.setBackgroundResource(images[position]);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,MenuItemActivity.class);
                intent.putExtra("MenuItem",holder.menuTxt.getText().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return models.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView menuTxt;
        ImageView menuImg;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            menuTxt = itemView.findViewById(R.id.menuTxt);
            menuImg = itemView.findViewById(R.id.menuImg);
            cardView = itemView.findViewById(R.id.cardMenu);


        }
    }

}