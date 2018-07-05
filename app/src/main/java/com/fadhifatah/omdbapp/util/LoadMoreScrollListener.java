package com.fadhifatah.omdbapp.util;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class LoadMoreScrollListener extends RecyclerView.OnScrollListener{

    private RecyclerView.LayoutManager manager;
    private int previousTotalItemCount = 0;
    private boolean isLoading = true;

    public LoadMoreScrollListener(GridLayoutManager manager) {
        this.manager = manager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (dy > 0) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            int visibleItemCount = gridLayoutManager.getChildCount();
            int totalItemCount = gridLayoutManager.getItemCount();
            int firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();

            if (totalItemCount < previousTotalItemCount) {
                previousTotalItemCount = totalItemCount;
                if (totalItemCount == 0)
                    isLoading = true;
            }

            if (isLoading && (totalItemCount > previousTotalItemCount)) {
                isLoading = false;
                previousTotalItemCount = totalItemCount;
            }

            if (!isLoading && ((visibleItemCount + firstVisibleItem) >= totalItemCount)) {
                isLoading = true;
                onLoadMore();
            }
        }
    }

    public void resetState() {
        this.previousTotalItemCount = 0;
        this.isLoading = true;
    }

    protected abstract void onLoadMore();
}
