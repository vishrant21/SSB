package com.example.loginanimation;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeCallback extends ItemTouchHelper.Callback {
    private final SwipeListener swipeListener;

    public SwipeCallback(SwipeListener listener) {
        swipeListener = listener;
    }


    // Specify the swipe directions (right-to-left in this case)
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.RIGHT);
    }

    // Handle item swipe
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false; // We're not interested in drag-and-drop
    }

    // Handle item swipe for deletion
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (direction == ItemTouchHelper.RIGHT) {
            // Notify the listener to perform the action (e.g., delete item)
            swipeListener.onSwipeRight(viewHolder.getAdapterPosition());
        }
    }
}

