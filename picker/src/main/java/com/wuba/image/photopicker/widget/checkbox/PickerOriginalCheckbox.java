package com.wuba.image.photopicker.widget.checkbox;

import android.content.Context;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;

/**
 * desc : origin image check box
 * date : 2018/8/17
 *
 * @author : dongSen
 */
public class PickerOriginalCheckbox extends AppCompatRadioButton {

    public PickerOriginalCheckbox(Context context) {
        super(context);
        init();
    }

    public PickerOriginalCheckbox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PickerOriginalCheckbox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
    }

    @Override
    public void toggle() {
        setChecked(!isChecked());
    }
}
