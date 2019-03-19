package com.wuba.image.photopicker.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.LinkedList;
import java.util.List;

/**
 * desc : image folder model
 * date : 2018/8/14
 *
 * @author : dongSen
 */
public class ImageFolder implements Parcelable {

    private String name;

    private String coverPath;

    private List<String> mImages = new LinkedList<>();

    public ImageFolder(String name) {
        this.name = name;
    }

    public ImageFolder(String name, String coverPath) {
        this.name = name;
        this.coverPath = coverPath;
    }

    /**
     * add image
     */
    public void addLastImage(String imagePath) {
        mImages.add(imagePath);
    }

    public List<String> getImages() {
        return mImages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverPath() {
        return coverPath;
    }

    /**
     * 取第一张的图片作为封面
     * @param coverPath 图片路径
     */
    public void setCoverPath(String coverPath) {
        if (this.coverPath != null || coverPath == null)
            return;

        this.coverPath = coverPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.coverPath);
        dest.writeStringList(this.mImages);
    }

    protected ImageFolder(Parcel in) {
        this.name = in.readString();
        this.coverPath = in.readString();
        this.mImages = in.createStringArrayList();
    }

    public static final Creator<ImageFolder> CREATOR = new Creator<ImageFolder>() {
        @Override
        public ImageFolder createFromParcel(Parcel source) {
            return new ImageFolder(source);
        }

        @Override
        public ImageFolder[] newArray(int size) {
            return new ImageFolder[size];
        }
    };
}
