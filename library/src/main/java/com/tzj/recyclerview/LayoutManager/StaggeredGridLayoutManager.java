package com.tzj.recyclerview.LayoutManager;

/**
 * Created by tzj on 2018/5/30.
 */

public class StaggeredGridLayoutManager extends android.support.v7.widget.StaggeredGridLayoutManager implements ILayoutManager {
    public StaggeredGridLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    @Override
    public Span getSpan(int count, int index) {
        return new Span(count,index,index/getSpanCount(),index%getSpanCount(),getSpanCount(),1,null);
    }
}
