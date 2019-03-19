package com.wuba.image.photopicker.util;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * author : dongSen
 * date : 2018/8/15 3:37 PM
 * desc : list item decoration
 */
public class PickerSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;

    public PickerSpaceItemDecoration(Context context, @DimenRes int dimenRes) {
        mSpace = context.getResources().getDimensionPixelSize(dimenRes);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = mSpace;
        outRect.right = mSpace;
    }
}