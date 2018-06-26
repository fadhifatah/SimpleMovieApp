package com.fadhifatah.omdbapp.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Fatah on 7/7/2017.
 */

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
    private int spanCount;
    private int spacing;
    private boolean includingEdge;

    public GridSpacingItemDecoration(int spanCount, int dpToPx, boolean b) {
        this.spanCount = spanCount;
        this.spacing = dpToPx;
        this.includingEdge = b;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildAdapterPosition(view);
        int column = position % spanCount;

        if (includingEdge) {
            outRect.left = spacing - column * spacing / spanCount;
            outRect.right = (column + 1) * spacing / spanCount;
            outRect.top = (position < spanCount) ? spacing : outRect.top;
            outRect.bottom = spacing;
        }
        else {
            outRect.left = column * spacing / spanCount;
            outRect.right = spacing - (column + 1) * spacing / spanCount;
            outRect.top = (position >= spanCount) ? spacing : outRect.top;
        }
    }
}
