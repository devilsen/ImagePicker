package com.wuba.image.photopicker.widget.checkbox;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;

/**
 * desc : customize check box
 * date : 2018/8/15
 *
 * @author : dongSen
 */
public class PickerCheckBox extends AppCompatCheckBox{

    public PickerCheckBox(Context context) {
        super(context);
        init(context);
    }

    public PickerCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PickerCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){

    }
}
