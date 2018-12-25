package com.glee.mxrecyclerview;

import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class Attribute {
    private @RecyclerView.Orientation
    int orientation = RecyclerView.VERTICAL;

    private boolean refreshable;

    private boolean loadable;

    static final String LINEAR_LAYOUT_MANAGER = "LinearLayoutManager";
    static final String GRID_LAYOUT_MANAGER = "GridLayoutManager";
    private String layoutManager;
    private int spanCount = 2;
    private boolean reverseLayout;
    @Nullable
    static Attribute getAttr(AttributeSet attributeSet) {
        return null;
    }


    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public boolean isRefreshable() {
        return refreshable;
    }

    public void setRefreshable(boolean refreshable) {
        this.refreshable = refreshable;
    }

    public boolean isLoadable() {
        return loadable;
    }

    public void setLoadable(boolean loadable) {
        this.loadable = loadable;
    }

    public static String getLinearLayoutManager() {
        return LINEAR_LAYOUT_MANAGER;
    }

    public static String getGridLayoutManager() {
        return GRID_LAYOUT_MANAGER;
    }

    public String getLayoutManager() {
        return layoutManager;
    }

    public void setLayoutManager(String layoutManager) {
        this.layoutManager = layoutManager;
    }

    public int getSpanCount() {
        return spanCount;
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }

    public boolean isReverseLayout() {
        return reverseLayout;
    }

    public void setReverseLayout(boolean reverseLayout) {
        this.reverseLayout = reverseLayout;
    }
}
