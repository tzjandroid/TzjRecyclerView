package com.tzj;

import android.graphics.Canvas;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.tzj.recyclerview.IViewType;
import com.tzj.recyclerview.adapter.AdapterDelegate;

import java.util.Collections;
import java.util.List;

/**
 * RecyclerView 的交换
 */
public class DrawCallBack extends ItemTouchHelper.Callback {

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = 0;
        int swipeFlags = 0;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            if (((LinearLayoutManager) layoutManager).getOrientation() == LinearLayoutManager.VERTICAL) {
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            } else {
                dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            }
        } else {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            swipeFlags = 0;
        }
        return makeMovementFlags(dragFlags, swipeFlags); //该方法指定可进行的操作
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if (viewHolder.getItemViewType() == target.getItemViewType()) {
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if (adapter instanceof AdapterDelegate) {
                List<? extends IViewType> list = ((AdapterDelegate) adapter).getAdapter().getList();
                Collections.swap(list, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }
        }
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            if (isCurrentlyActive) {
                viewHolder.itemView.setScaleX(0.9f);
                viewHolder.itemView.setScaleY(0.9f);
            } else {
                viewHolder.itemView.setScaleX(1f);
                viewHolder.itemView.setScaleY(1f);
            }
        }
    }

    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setScaleX(1f);
        viewHolder.itemView.setScaleY(1f);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }
}