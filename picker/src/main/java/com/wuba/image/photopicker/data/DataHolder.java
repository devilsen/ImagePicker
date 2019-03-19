package com.wuba.image.photopicker.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.wuba.mobile.lib.analysis.AnalysisCenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * desc : data center
 * date : 2018/8/15
 *
 * @author : dongSen
 */
public class DataHolder {

    public static DataHolder getInstance() {
        return LazyHolder.instance;
    }

    private static class LazyHolder {
        private static final DataHolder instance = new DataHolder();
    }

    private DataHolder() {
        imageList = new ArrayList<>();
        selectList = new ArrayList<>();
        selectMap = new HashMap<>();
    }

    public static final String EXTRA_SELECTED_IMAGES = "EXTRA_SELECTED_IMAGES";
    public static final String EXTRA_IS_FULL = "EXTRA_IS_FULL";

    /**
     * 当前选中文件夹中的图片
     */
    private final ArrayList<String> imageList;

    /**
     * 已选图片
     */
    private ArrayList<String> selectList;

    private final HashMap<String, Boolean> selectMap;

    /**
     * 当前展示图片位置
     */
    private int currentPosition;

    private int maxSize = 9;

    private boolean isOriginal = false;
    /**
     * 选取头像
     */
    private boolean pickAvatar;

    public ArrayList<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList.clear();
        this.imageList.addAll(imageList);
    }

    public ArrayList<String> getSelectList() {
        return selectList;
    }

    public void setSelectList(ArrayList<String> selectList) {
        this.selectList = selectList;
    }

    public boolean addSelectImage(String imagePath) {
        if (getSelectListSize() + 1 > getMaxSize())
            return false;

        selectList.add(imagePath);
        selectMap.put(imagePath, true);

        return true;
    }

    public void removeSelectImage(String imagePath) {
        selectList.remove(imagePath);
        selectMap.remove(imagePath);
    }

    public int getSelectListSize() {
        return selectList.size();
    }

    public void clearSelect(Context context) {
        selectList.clear();
        selectMap.clear();

        if (context != null) {
            Properties properties = new Properties();
            properties.put("type", isOriginal ? "原图" : "普通");
            AnalysisCenter.onEvent(context, AnalysisConstants.IM_PHOTO_MESSAGE_SEND, properties);
        }
        isOriginal = false;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    /**
     * 获取当前选中图片
     *
     * @param position 图片位置
     * @return 图片路径
     */
    public String getCurrentImage(int position) {
        return imageList.get(position);
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    /**
     * 是否已经被选择
     *
     * @param position 位于列表中的位置
     */
    public boolean imageIsSelected(int position) {
        String imagePath = imageList.get(position);

        return imageIsSelected(imagePath);
    }

    /**
     * 是否已经被选择
     *
     * @param imagePath 图片路径
     */
    public boolean imageIsSelected(String imagePath) {
        Boolean checked = selectMap.get(imagePath);

        if (checked == null) {
            return false;
        } else {
            return checked;
        }
    }

    public void setPickMode(boolean pickAvatar) {
        this.pickAvatar = pickAvatar;
    }

    public boolean isPickAvatar() {
        return pickAvatar;
    }

    public void setMaxSize(int size) {
        maxSize = size;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public boolean isOriginal() {
        return isOriginal;
    }

    public void setOriginal(boolean original) {
        isOriginal = original;
    }

    public void returnSelectImages(Activity activity) {
        Intent data = new Intent();
        ArrayList<String> selectImages = new ArrayList<>(DataHolder.getInstance().getSelectList());
        data.putExtra(DataHolder.EXTRA_SELECTED_IMAGES, selectImages);
        data.putExtra(DataHolder.EXTRA_IS_FULL, isOriginal());
        activity.setResult(Activity.RESULT_OK, data);
        clearSelect(activity);
    }

}
