package com.tzj.recyclerview.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.TzjSpanSizeLookup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.swipe.SwipeLayout;
import com.tzj.ISwipeViewType;
import com.tzj.listener.NoDoubleOnClickListener;
import com.tzj.recyclerview.IViewType;
import com.tzj.recyclerview.R;
import com.tzj.recyclerview.holder.TzjViewHolder;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class TzjAdapter extends RecyclerView.Adapter<TzjViewHolder> {
    /**
     * 记录暂存用
     */
    private RecyclerView.LayoutManager layoutManager;
    /**
     * 如果不为空，用这里的内容
     */
    private ISwipeViewType viewType;
    /**
     * view 的点击事件用 Tag 取index吗？
     */
    protected boolean tagIndex = true;
    /**
     * 数据
     */
    private List<Object> mData = new ArrayList();
    /**
     * 记录上次点击的地方
     */
    private int selectId = -1;
    /**
     * item 点击事件
     */
    private OnItemClickListener itemClickListener;
    /**
     * item 的点事件
     */
    private View.OnClickListener itemListenerRelay = new NoDoubleOnClickListener() {
        @Override
        public void onMyClick(View v) {
            Integer i = (Integer) v.getTag(R.id.item_index_tag);
            if (i != null && itemClickListener != null) {
                if (v instanceof SwipeLayout){
                    SwipeLayout swipeLayout = (SwipeLayout) v;
                    //关闭才可以点击
                    if (swipeLayout.getOpenStatus() != SwipeLayout.Status.Close){
                        return;
                    }
                }
                setSelectId(i);
                itemClickListener.onItemClick(TzjAdapter.this, v, i, getItem(i));
            }
        }
    };
    /**
     * item 长按
     */
    private OnItemLongClickListener itemLongClickListener;
    /**
     * item 长按事件
     */
    private View.OnLongClickListener itemLongListenerRelay = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            Integer i = (Integer) v.getTag(R.id.item_index_tag);
            if (i != null && itemLongClickListener != null) {
                if (v instanceof SwipeLayout){
                    SwipeLayout swipeLayout = (SwipeLayout) v;
                    //关闭才可以点击
                    if (swipeLayout.getOpenStatus() != SwipeLayout.Status.Close){
                        return false;
                    }
                }
                itemLongClickListener.onItemLongClick(TzjAdapter.this, v, i, getItem(i));
                return true;
            }
            return false;
        }
    };
    /**
     * view 点击事件
     */
    private OnClickIndexListener clickListener;
    /**
     * view 的点击事件
     */
    private View.OnClickListener listenerRelay = new NoDoubleOnClickListener() {
        @Override
        public void onMyClick(View v) {
            Integer i = (Integer) v.getTag(R.id.item_index_tag);
            if (i != null && clickListener != null) {
                clickListener.onClick(v, i);
            }
        }
    };

    public RecyclerView.LayoutManager getLayoutManager() {
        return layoutManager;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public void setTagIndex(boolean tagIndex) {
        this.tagIndex = tagIndex;
    }

    public IViewType getViewType() {
        return viewType;
    }

    public void setViewType(ISwipeViewType viewType) {
        this.viewType = viewType;
    }

    public List<?> getList() {
        return mData;
    }

    public void setList(List<?> list) {
        this.mData = (List<Object>) list;
    }

    public void addList(List list) {
        this.mData.addAll(list);
    }

    public void addItem(Object item) {
        this.mData.add(item);
    }

    public void setSelectId(int index) {
        this.selectId = index;
    }

    public int getSelectId() {
        return selectId;
    }

    /**
     * item 点击事件
     */
    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /**
     * item 长按事件
     */
    public void setItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

    /**
     * 点了具体的 view
     */
    public void setClickListener(OnClickIndexListener clickListener) {
        this.clickListener = clickListener;
    }

    public <T> T getItem(int position) {
        return (T) mData.get(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 记录 {@link #getItemViewType(int position)} 里的 position
     * 用于 {@link #onCreateViewHolder(ViewGroup parent, int viewType)} 里获取 Class<? extends TzjViewHolder>
     */
    private int lastItemViewTypePosition;

    @Override
    public int getItemViewType(int position) {
        lastItemViewTypePosition = position;
        return getViewId(position);
    }

    public int getViewId(int index) {
        if (viewType != null) {
            return viewType.type();
        } else {
            Object item = getItem(lastItemViewTypePosition);
            if (item instanceof IViewType) {
                return ((IViewType) item).type();
            } else {
                throw new RuntimeException("请设置 viewType 或者 集合类实现IViewType");
            }
        }
    }

    public int getSwipeLayoutResourceId(int position) {
        if (viewType != null) {
            return viewType.getSwipeLayoutResourceId(position);
        } else {
            Object item = getItem(lastItemViewTypePosition);
            if (item instanceof ISwipeViewType) {
                return ((ISwipeViewType) item).getSwipeLayoutResourceId(position);
            } else {
                return 0;
            }
        }
    }

    public Class<? extends TzjViewHolder> getHolder(int index) {
        if (viewType != null) {
            return viewType.holder();
        } else {
            Object item = getItem(lastItemViewTypePosition);
            if (item instanceof IViewType) {
                return ((IViewType) item).holder();
            } else {
                throw new RuntimeException("请设置 viewType 或者 集合类实现IViewType");
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public TzjViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TzjViewHolder holder = null;
        View inflate = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        Class<? extends TzjViewHolder> holer = getHolder(lastItemViewTypePosition);
        try {
            Constructor<? extends TzjViewHolder> constructor = holer.getConstructor(View.class);
            holder = constructor.newInstance(inflate);
            holder.onCreateView(parent.getContext(), this, holder.itemView);
            holder.setListener(listenerRelay);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //为了解决 SwipeLayout的不能点击问题
        if (getSwipeLayoutResourceId(lastItemViewTypePosition) != 0) {
            SwipeLayout swipeLayout = (SwipeLayout) holder.itemView.findViewById(getSwipeLayoutResourceId(lastItemViewTypePosition));
            holder.setSwipeLayout(swipeLayout);
            if (holder.onClickable()){
                swipeLayout.setOnClickListener(itemListenerRelay);
                swipeLayout.setOnLongClickListener(itemLongListenerRelay);
            }
        } else {
            if (holder.onClickable()){
                holder.itemView.setOnClickListener(itemListenerRelay);
                holder.itemView.setOnLongClickListener(itemLongListenerRelay);
            }
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TzjViewHolder holder, int position) {
        //为了解决 SwipeLayout的不能点击问题
        SwipeLayout swipeLayout = holder.getSwipeLayout();
        if (swipeLayout != null) {
            swipeLayout.setTag(R.id.item_index_tag, position);
        } else {
            holder.itemView.setTag(R.id.item_index_tag, position);
        }
        holder.onBind(this, getItem(position), position);
        if (!holder.isFirstBinded){
            //第一次
            holder.isFirstBinded = true;
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new TzjSpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    IViewType item = getItem(position);
                    if (item instanceof com.tzj.recyclerview.LayoutManager.GridLayoutManager.SpanSize) {
                        return ((com.tzj.recyclerview.LayoutManager.GridLayoutManager.SpanSize) item).getSpanSize();
                    } else {
                        return 1;
                    }
                }
            });
        }
    }

    /**
     * itemClick
     */
    public interface OnItemClickListener<T> {
        void onItemClick(TzjAdapter adapter, View v, int index, T obj);
    }
    /**
     * onItemLongClick
     */
    public interface OnItemLongClickListener<T> {
        void onItemLongClick(TzjAdapter adapter, View v, int index, T obj);
    }

    /**
     * 返回index的 click
     */
    public static abstract class OnClickIndexListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
//            onClick(v); 没用了被废弃了
        }

        public abstract void onClick(View v, int index);
    }
}
