package com.glee.mxrecyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.Objects;

public class RefreshAndLoadRecyclerView extends SmartRefreshLayout {
    interface OnRefreshListener {
        void onRefresh();
    }

    interface OnLoadListener {
        void onLoad();
    }

    private LayoutInflater inflater;
    private Context context;
    private RecyclerView recyclerView;
    private OnRefreshListener onRefreshListener;
    private OnLoadListener onLoadListener;

    public RefreshAndLoadRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public RefreshAndLoadRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshAndLoadRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(Attribute.getAttr(attrs));
    }

    private void init(@Nullable Attribute attribute) {
        context = getContext().getApplicationContext();
        inflater = LayoutInflater.from(context);
        recyclerView = new RecyclerView(getContext());
        RecyclerView.LayoutManager layoutManager;

        if (attribute == null || Objects.equals(attribute.getLayoutManager(), Attribute.LINEAR_LAYOUT_MANAGER)) {
            layoutManager = new LinearLayoutManager(
                    context,
                    attribute == null ? RecyclerView.VERTICAL : attribute.getOrientation(),
                    /*
                    attribute == null ? false : attribute.isReverseLayout()
                                           simplify↓                        */
                    attribute != null && attribute.isReverseLayout()
            );
        } else {
            layoutManager = new GridLayoutManager(context, attribute.getSpanCount(), attribute.getOrientation(), attribute.isReverseLayout());
        }

        recyclerView.setLayoutManager(layoutManager);
        addView(recyclerView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        recyclerView.setAdapter(getAdapter());
    }

    private void init(final Options options) {
        if (options.onRefreshListener != null) {
            setOnRefreshListener(new com.scwang.smartrefresh.layout.listener.OnRefreshListener() {
                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    options.onRefreshListener.onRefresh();
                }
            });
        }
    }

    private RecyclerView.Adapter getAdapter() {
        return null;
    }

    public <T> Options beginOption() {
        return new Options(this);
    }

    private static class Options {
        RecyclerView.LayoutManager layoutManager;
        RefreshAndLoadRecyclerView recyclerView;
        OnRefreshListener onRefreshListener;
        OnLoadListener onLoadListener;
        DiffUtil.ItemCallback itemCallback;

        private Options(RefreshAndLoadRecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        public void commit() {
            recyclerView.init(this);
        }

        public Options setLayoutManager(final RecyclerView.LayoutManager layoutManager) {
            this.layoutManager = layoutManager;
            if (layoutManager instanceof GridLayoutManager) {
                final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                if (gridLayoutManager.getSpanSizeLookup() instanceof GridLayoutManager.DefaultSpanSizeLookup) {
                    gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            return 1;
                        }
                    });
                }
            }
            return this;
        }

        public Options setRefreshListener(OnRefreshListener onRefreshListener) {
            this.onRefreshListener = onRefreshListener;
            return this;
        }

        public Options setLoadListener(OnLoadListener onLoadListener) {
            this.onLoadListener = onLoadListener;
            return this;
        }

        public <T> Options setDiffCallback(DiffUtil.ItemCallback<T> itemCallback) {
            this.itemCallback = itemCallback;
            return this;
        }
    }
}
