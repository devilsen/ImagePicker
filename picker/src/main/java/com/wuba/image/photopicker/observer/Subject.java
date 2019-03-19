package com.wuba.image.photopicker.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * desc :
 * date : 2018/8/23
 *
 * @author : dongSen
 */
abstract class Subject {

    private List<PickerObserver> obs = new ArrayList<>();

    public void addObserver(PickerObserver obs) {
        this.obs.add(obs);
    }

    public void removeObserver(PickerObserver obs) {
        this.obs.remove(obs);
    }

    void notifyObserver() {
        for (PickerObserver o : obs) {
            o.update();
        }
    }

    public abstract void notifyPicker();

}
