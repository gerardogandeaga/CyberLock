package com.gerardogandeaga.cyberlock.lists.decorations;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author gerardogandeaga
 * created on 2018-08-10
 */
public class PhotoItemDecoration extends RecyclerView.ItemDecoration {
    private int mSpanCount;
    private int mSpacing;

    public PhotoItemDecoration(int spanCount, int spacing) {
        mSpanCount = spanCount;
        mSpacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int column = position % mSpanCount;

        if (position < mSpanCount) {
            outRect.top = mSpacing;
        }
        outRect.bottom = mSpacing;

        outRect.left = mSpacing - column * mSpacing / mSpanCount;
        outRect.right = (column + 1) * mSpacing / mSpanCount;
    }
}
