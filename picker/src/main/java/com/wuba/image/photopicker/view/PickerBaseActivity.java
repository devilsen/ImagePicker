package com.wuba.image.photopicker.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.wuba.image.photopicker.R;

/**
 * desc :
 * date : 2018/8/17
 *
 * @author : dongSen
 */
public class PickerBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.image_picker_activity_enter, 0);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.image_picker_activity_exit);
    }
}
