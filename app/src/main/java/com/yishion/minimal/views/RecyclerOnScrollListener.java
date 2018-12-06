package com.yishion.minimal.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

public abstract class RecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    private static final int DISTANCE = 20;
    private boolean isVisible;//默认时可见的
    private int mScrollYDistance;//Y滚动的距离

    public RecyclerOnScrollListener() {
        super();
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

    }

    //dy>0向上，dy<0向下
    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        mScrollYDistance += dy;

//        if (mScrollYDistance > DISTANCE) {//超距离
//            if (isVisible) {//可见，之后，
//                isVisible = false;
//                hide();
//            }
//        }
//        else {
//            if (!isVisible) {
//                isVisible = true;
//                show();
//            }
//        }

        //上面那样写也Ok,好理解
        if (isVisible && mScrollYDistance > DISTANCE) {//超过范围，并且可见
            isVisible = false;
            hide();
        }
        else if (!isVisible && mScrollYDistance <= DISTANCE) {//小于范围，并且不可见
            isVisible = true;
            show();
        }

    }

    public abstract void hide();

    public abstract void show();
}
