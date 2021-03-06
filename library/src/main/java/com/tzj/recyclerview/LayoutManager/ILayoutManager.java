package com.tzj.recyclerview.LayoutManager;

/**
 * Created by Administrator on 2018/5/21.
 */
public interface ILayoutManager {
    /**
     * 方向
     */
    int getOrientation();

    /**
     * 设置方向
     */
    void setOrientation(int orientation);

    /**
     * 一行分几个
     */
    int getSpanCount();

    /**
     * 设置一行几个
     */
    void setSpanCount(int spanCount);

    /**
     * 设置一行几个
     */
    Span getSpan(int count,int index);
}
