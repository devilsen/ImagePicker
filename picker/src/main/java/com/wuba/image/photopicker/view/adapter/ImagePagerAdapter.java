package com.wuba.image.photopicker.view.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.wuba.mobile.photoview.OnViewTapListener;
import com.wuba.mobile.photoview.PhotoView;

import java.util.ArrayList;

/**
 * desc : viewpager adapter
 * date : 2018/8/15
 *
 * @author : dongSen
 */
public class ImagePagerAdapter extends PagerAdapter {

    private ArrayList<String> listData;

    private OnViewTapListener viewTapListener;

    public ImagePagerAdapter(ArrayList<String> listData, OnViewTapListener onViewTapListener) {
        this.listData = listData;
        this.viewTapListener = onViewTapListener;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        PhotoView imageView = new PhotoView(container.getContext());
        container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        imageView.setOnViewTapListener(viewTapListener);

        Glide.with(container.getContext())
                .load(listData.get(position))
                .dontAnimate()
                .into(imageView);

        return imageView;
    }

    @Override
    public int getCount() {
        return listData == null ? 0 : listData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
