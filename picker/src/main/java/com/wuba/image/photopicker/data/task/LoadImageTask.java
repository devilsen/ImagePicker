package com.wuba.image.photopicker.data.task;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.wuba.image.photopicker.data.ImageFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * author : dongSen
 * date : 2018/8/14 5:08 PM
 * desc : get image from phone
 */
public class LoadImageTask extends ImageAsyncTask<Void, ArrayList<ImageFolder>> {

    private ContentResolver resolver;

    public LoadImageTask(ContentResolver resolver, Callback<ArrayList<ImageFolder>> callback) {
        super(callback);
        this.resolver = resolver;
    }

    @Override
    protected ArrayList<ImageFolder> doInBackground(Void... voids) {

        ArrayList<ImageFolder> folderList = new ArrayList<>();

        if (resolver == null)
            return folderList;

        Cursor cursor = resolver.query(
                QueryParam.getUri(),
                QueryParam.getProjection(),
                QueryParam.getSelection(),
                QueryParam.getSelectionArgs(),
                QueryParam.getOrder()
        );

        if (cursor == null) {
            return folderList;
        }

        if (cursor.getCount() <= 0) {
            cursor.close();
            return folderList;
        }

        ImageFolder totalImageFolder = new ImageFolder("所有图片");
        folderList.add(totalImageFolder);

        HashMap<String, ImageFolder> imageFolderMap = new HashMap<>();

        while (cursor.moveToNext()) {
            String imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

            if (isNotImageFile(imagePath)) {
                continue;
            }

            //设置目录封面
            totalImageFolder.setCoverPath(imagePath);
            //所有图片目录,每次都添加
            totalImageFolder.addLastImage(imagePath);
            //获取文件夹路径
            String folderPath = getFolderPath(imagePath);
            if (TextUtils.isEmpty(folderPath)) {
                continue;
            }

            ImageFolder folder = imageFolderMap.get(folderPath);
            if (folder == null) {
                String folderName = getFolderName(folderPath);
                folder = new ImageFolder(folderName, imagePath);
                imageFolderMap.put(folderPath, folder);
            }
            folder.addLastImage(imagePath);
        }

        // 添加其他图片目录
        folderList.addAll(imageFolderMap.values());

        cursor.close();

        return folderList;
    }

    /**
     * get folder name
     */
    private String getFolderName(String folderPath) {
        String folderName = folderPath.substring(folderPath.lastIndexOf(File.separator) + 1);
        if (TextUtils.isEmpty(folderName)) {
            folderName = "/";
        }
        return folderName;
    }

    /**
     * get folder path
     */
    private String getFolderPath(@NonNull String imagePath) {
        String folderPath = null;

        int end = imagePath.lastIndexOf(File.separator);
        if (end != -1) {
            folderPath = imagePath.substring(0, end);
        }

        return folderPath;
    }

    private boolean isNotImageFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return true;
        }
        File file = new File(path);
        return !file.exists() || file.length() == 0;
    }

    public LoadImageTask perform() {
        executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return this;
    }
}
