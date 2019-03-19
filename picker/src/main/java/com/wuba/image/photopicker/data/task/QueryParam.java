package com.wuba.image.photopicker.data.task;

import android.net.Uri;
import android.provider.MediaStore;

/**
 * desc : the params of resolver
 * date : 2018/8/14
 *
 * @author : dongSen
 */
class QueryParam {

    static Uri getUri() {
        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }

    static String[] getProjection() {
        return new String[]{MediaStore.Images.Media.DATA};
    }

    static String getSelection() {
        return MediaStore.Images.Media.MIME_TYPE + "=? or " +
                MediaStore.Images.Media.MIME_TYPE + "=? or " +
                MediaStore.Images.Media.MIME_TYPE + "=? or " +
                MediaStore.Images.Media.MIME_TYPE + "=?";
    }

    static String[] getSelectionArgs() {
        return new String[]{"image/jpeg", "image/png", "image/jpg", "image/gif"};
    }

    static String getOrder() {
        return MediaStore.Images.Media.DATE_ADDED + " DESC";
    }
}
