package com.wuba.image.photopicker;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.wuba.image.photopicker.view.PickerActivity;

import java.io.File;

import static com.wuba.image.photopicker.data.PickerConstants.INTENT_NAME_PICK_AVATAR;
import static com.wuba.image.photopicker.data.PickerConstants.INTENT_NAME_SELECT_NUM;

/**
 * desc :
 * date : 2018/8/14
 *
 * @author : dongSen
 */
public class ImagePickerApi {

    private static File photoDir;

    private static File saveDir;

    public static void init(@NonNull String photoDirPath, @NonNull String saveDirPath) {
        if (photoDir == null)
            photoDir = new File(photoDirPath);

        if (saveDir == null)
            saveDir = new File(saveDirPath);
    }

    /**
     * 选取图片，默认为9张
     *
     * @return intent
     */
    public static Intent pickerIntent(Context context) {
        return pickerIntent(context, 9);
    }

    /**
     * 选取指定张数
     *
     * @return intent
     */
    public static Intent pickerIntent(Context context, int num) {
        Intent intent = new Intent(context, PickerActivity.class);
        intent.putExtra(INTENT_NAME_SELECT_NUM, num);
        return intent;
    }

    /**
     * 选取指定张数
     *
     * @return intent
     */
    public static Intent pickerAvatarIntent(Context context) {
        Intent intent = new Intent(context, PickerActivity.class);
        intent.putExtra(INTENT_NAME_SELECT_NUM, 1);
        intent.putExtra(INTENT_NAME_PICK_AVATAR, true);
        return intent;
    }

}
