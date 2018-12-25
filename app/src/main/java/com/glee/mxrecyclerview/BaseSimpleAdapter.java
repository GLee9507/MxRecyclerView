package com.glee.mxrecyclerview;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.AdapterListUpdateCallback;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liji
 * @date 2018/12/24 9:38
 * description
 */


public abstract class BaseSimpleAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    private final AsyncListDiffer<T> mHelper;
    private final @LayoutRes
    int layoutRes;
    private LayoutInflater inflater;
    private ArrayList<View> headers;
    private ArrayList<View> footers;
    private View loadMoreView;

    protected BaseSimpleAdapter(@LayoutRes int layoutRes, @NonNull DiffUtil.ItemCallback<T> diffCallback) {
        @SuppressLint("RestrictedApi") AsyncDifferConfig<T> config = new AsyncDifferConfig.Builder<>(diffCallback)
                .setBackgroundThreadExecutor(TaskExecutor.getIoExecutor())
                .setMainThreadExecutor(TaskExecutor.getMainExecutor())
                .build();
        mHelper = new AsyncListDiffer<>(new AdapterListUpdateCallback(this), config);
        this.layoutRes = layoutRes;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (inflater == null) {
            inflater = LayoutInflater.from(recyclerView.getContext());
        }
    }

    public void submitList(@Nullable List<T> list) {
        this.mHelper.submitList(list);
    }

    private T getItem(int position) {
        return this.mHelper.getCurrentList().get(position);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == TYPE_ITEM) {
            return new BaseViewHolder(inflater.inflate(layoutRes, viewGroup, false));
        } else if (i == TYPE_HEADER) {
            return new BaseViewHolder(new FrameLayout(viewGroup.getContext()), TYPE_HEADER);
        } else {
            return new BaseViewHolder(new FrameLayout(viewGroup.getContext()), TYPE_FOOTER);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder viewHolder, int i) {
        int itemViewType = getItemViewType(i);
        if (itemViewType == TYPE_HEADER) {
            View view = headers.get(i);
            ((ViewGroup) viewHolder.itemView).addView(view);
        } else if (itemViewType == TYPE_ITEM) {
            checkLoadMore(i);
            onBind(viewHolder, getItem(i - getHeaderCount()));
        } else if (itemViewType == TYPE_FOOTER) {
            View view = footers.get(i - getHeaderCount() - getContentCount());
            ((ViewGroup) viewHolder.itemView).addView(view);
        }
    }

    @Override
    public void onViewRecycled(@NonNull BaseViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder.getViewType() != TYPE_ITEM) {
            ((ViewGroup) holder.itemView).removeAllViews();
        }
    }

    public static final int TYPE_HEADER = -1;
    public static final int TYPE_FOOTER = 1;
    public static final int TYPE_ITEM = 0;


    @Override
    public int getItemViewType(int position) {
        if (position < getHeaderCount()) {
            return TYPE_HEADER;
        } else if (position < getHeaderCount() + getContentCount()) {
            return TYPE_ITEM;
        } else {
            return TYPE_FOOTER;
        }
    }

    abstract void onBind(BaseViewHolder viewHolder, T t);

    @Override
    public int getItemCount() {
        return this.mHelper.getCurrentList().size() + getHeaderCount() + getFooterCount();
    }

    public int getContentCount() {
        return this.mHelper.getCurrentList().size();
    }

    public int getHeaderCount() {
        return headers == null ? 0 : headers.size();
    }

    public int getFooterCount() {
        return footers == null ? 0 : footers.size();
    }

    public void addHeader(View header) {
        if (headers == null) {
            headers = new ArrayList<>(2);
        }
        if (!headers.contains(header)) {
            headers.add(header);
            notifyItemInserted(headers.size() - 1);
        }
    }

    public void addHeader(int index, View header) {
        if (headers == null) {
            headers = new ArrayList<>(2);
        }
        if (!headers.contains(header)) {
            headers.add(index, header);
            notifyItemInserted(index);
        }
    }

    public void addFooter(View footer) {
        if (footers == null) {
            footers = new ArrayList<>(2);
        }
        if (!footers.contains(footer)) {
            footers.add(footer);
            notifyItemInserted(getHeaderCount() + getContentCount() + footers.size() - 1);
        }
    }

    public void addFooter(int index, View footer) {
        if (footers == null) {
            footers = new ArrayList<>(2);
        }
        if (!footers.contains(footer)) {
            footers.add(index, footer);
            notifyItemInserted(getHeaderCount() + getContentCount() + index);
        }
    }

    public void removeFooter(int index) {
        footers.remove(index);
        notifyItemRemoved(getHeaderCount() + getContentCount() + index);
    }

    public void removeFooter(View footer) {
        int i = footers.indexOf(footer);
        if (i > -1) {
            footers.remove(i);
            notifyItemRemoved(getHeaderCount() + getContentCount() + i);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public void removeHeader(int index) {
        headers.remove(index);
        notifyItemRemoved(index);
    }

    public void removeHeader(View header) {
        int i = headers.indexOf(header);
        if (i > -1) {
            headers.remove(i);
            notifyItemRemoved(i);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    private void checkLoadViewEmpty() {
        if (loadMoreView == null) {
            loadMoreView = inflater.inflate(R.layout.loadmore, null);
        }
    }

    private void checkLoadMore(int i) {

    }
}
