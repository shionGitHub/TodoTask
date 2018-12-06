package com.yishion.minimal.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Recycler支持当数据为空的时候显示的View
 */
public class RecyclerViewSupportEmptyView extends RecyclerView {

    public RecyclerViewSupportEmptyView(@NonNull Context context) {
        super(context);
    }

    public RecyclerViewSupportEmptyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewSupportEmptyView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private View mEmptyView;

    public void setEmptyView(View mEmptyView) {
        this.mEmptyView = mEmptyView;
    }

    //适配器数据变化时候的观察器
    AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            showEmptyView();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            showEmptyView();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            showEmptyView();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            showEmptyView();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            showEmptyView();
        }
    };

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
            observer.onChanged();
        }

    }


    @Override
    protected void onDetachedFromWindow() {
        Adapter adapter = getAdapter();
        if (adapter != null) {
            adapter.unregisterAdapterDataObserver(observer);
        }
        super.onDetachedFromWindow();
    }


    private void showEmptyView() {
        Adapter adapter = getAdapter();
        if (adapter != null) {
            int itemCount = adapter.getItemCount();
            if (itemCount > 0) {
                this.setVisibility(VISIBLE);
                if (this.mEmptyView != null) {
                    this.mEmptyView.setVisibility(View.GONE);
                }
            }
            else {
                this.setVisibility(INVISIBLE);
                if (this.mEmptyView != null) {
                    this.mEmptyView.setVisibility(View.VISIBLE);
                }
            }
        }

    }

}
