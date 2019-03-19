package com.wuba.image.photopicker.widget.popup;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.PopupWindow;

/**
 * desc :
 * date : 2018/8/16
 *
 * @author : dongSen
 */
abstract class BasePopupWindow extends PopupWindow {

    Context mContext;
    View mAnchorView;

    BasePopupWindow(Context context, @LayoutRes int layoutId, View anchorView, int width, int height) {
        super(View.inflate(context, layoutId, null), width, height, true);

        init(context, anchorView);

        initView();
    }

    private void init(Context context, View anchorView) {
//        getContentView().setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    dismiss();
//                    return true;
//                }
//                return false;
//            }
//        });
        // 如果想让在点击别的地方的时候 关闭掉弹出窗体 一定要记得给mPopupWindow设置一个背景资源
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mContext = context;
        mAnchorView = anchorView;
    }

    protected abstract void initView();

    public abstract void show();
}
