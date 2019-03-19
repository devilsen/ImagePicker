package com.wuba.image.photopicker;

import org.junit.Test;

import java.io.File;

/**
 * desc :
 * date : 2018/8/23
 *
 * @author : dongSen
 */
public class FileTest {

    private String mSaveImgDir = "image2/";

    @Test
    public void makeDir() {
        File file = new File(mSaveImgDir);
        if (!file.exists()) {
            file.mkdir();
        }
    }

}
