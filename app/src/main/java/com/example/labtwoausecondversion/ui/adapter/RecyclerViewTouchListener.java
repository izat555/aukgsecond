package com.example.labtwoausecondversion.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.labtwoausecondversion.ui.IRecyclerViewClickListener;

public class RecyclerViewTouchListener implements RecyclerView.OnItemTouchListener {
    private IRecyclerViewClickListener mRecyclerViewClickListener;
    private GestureDetector mGestureDetector;

    public RecyclerViewTouchListener(Context context,
                                     final RecyclerView recyclerView,
                                     IRecyclerViewClickListener recyclerViewClickListener) {
        mRecyclerViewClickListener = recyclerViewClickListener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && mRecyclerViewClickListener != null) {
                    mRecyclerViewClickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null && mRecyclerViewClickListener != null && mGestureDetector.onTouchEvent(e)) {
            mRecyclerViewClickListener.onClick(child, rv.getChildAdapterPosition(child));
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
