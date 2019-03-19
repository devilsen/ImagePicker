package com.wuba.image.photopicker.observer;

/**
 * desc :
 * date : 2018/8/23
 *
 * @author : dongSen
 */
public class PickerObserverHolder {

    private final PickerSubject subject = new PickerSubject();

    private PickerObserverHolder() {
    }

    public static PickerObserverHolder getInstance() {
        return Holder.instance;
    }

    private static class Holder {
        private static final PickerObserverHolder instance = new PickerObserverHolder();
    }

    public void addObserver(PickerObserver obs) {
        subject.addObserver(obs);
    }

    public void removeObserver(PickerObserver obs) {
        subject.removeObserver(obs);
    }

    public void notifyFinish() {
        subject.notifyPicker();
    }

}
