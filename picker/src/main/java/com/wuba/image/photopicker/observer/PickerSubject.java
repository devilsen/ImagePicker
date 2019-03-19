package com.wuba.image.photopicker.observer;

/**
 * desc :
 * date : 2018/8/23
 *
 * @author : dongSen
 */
public class PickerSubject extends Subject {
    @Override
    public void notifyPicker() {
        notifyObserver();
    }
}
