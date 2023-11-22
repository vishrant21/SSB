package com.example.loginanimation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MyViewHolder> {

    String[] images,myBreakfast;
    int myPrice[];
    int x;
    Context context;
    Boolean isAdminLoggedIn;
    MenuItemActivity menuItemActivity= new MenuItemActivity();

    public MenuItemAdapter(Context c, String[] breakfast,int[] price, String[] image,MenuItemActivity menuItemActivity,Boolean isAdminLoggedIn){
        myBreakfast = breakfast;
        myPrice = price;
        images = image;
        context = c;
        this.menuItemActivity = menuItemActivity;
        this.isAdminLoggedIn = isAdminLoggedIn;
    }
    @NonNull
    @Override
    public MenuItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.r_v_item2, parent ,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemAdapter.MyViewHolder holder, int position)
    {

        holder.lstTxt.setText(myBreakfast[position]);
        String p = Integer.toString(myPrice[position]);
        holder.lstPrice.setText(p);
        Picasso.get().load(images[position]).into(holder.lstImage);

        if (isAdminLoggedIn)
        {
            holder.lstLyt.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(menuItemActivity);
                    builder.setMessage("Are you sure you want to Delete the Item?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    menuItemActivity.afterLongClick(position);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.dismiss();
                                }
                            });

                    // Create and show the AlertDialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return true;
                }
            });
        }
        else{}
        holder.lstLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ItemActivity.class);
                intent.putExtra("posImage",images[holder.getAdapterPosition()]);
                intent.putExtra("posPrice",myPrice[holder.getAdapterPosition()]);
                intent.putExtra("posName",myBreakfast[holder.getAdapterPosition()]);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return myBreakfast.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView lstTxt,lstPrice;
        LinearLayout lstLyt;
        ImageView lstImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            lstTxt = itemView.findViewById(R.id.lstTxt);
            lstLyt = itemView.findViewById(R.id.lstLyt);
            lstPrice = itemView.findViewById(R.id.lstPrice);
            lstImage = itemView.findViewById(R.id.lstImage);

        }
    }

}
