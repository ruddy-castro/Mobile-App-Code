package com.example.project2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * This class is to store and share data between fragments
 */
public class SharedData extends ViewModel {

    private final MutableLiveData<Integer> selectedItem = new MutableLiveData<>();
    private final MutableLiveData<Integer> selectedItemIndex = new MutableLiveData<>();

    /**
     * To set the item in array.
     * @param item the item to be selected.
     */
    public void setSelectedItem(Integer item) {
        selectedItem.setValue(item);
    }

    /**
     * To get the selected item.
     * @return the selected item.
     */
    public LiveData<Integer> getSelectedItem() {
        return selectedItem;
    }

    /**
     * To set the index of the item.
     * @param index the index of the selected item.
     */
    public void setSelectedItemIndex(Integer index) {
        selectedItemIndex.postValue(index);
    }

    /**
     * To get the index of the selected item.
     * @return the index.
     */
    public LiveData<Integer> getSelectedItemIndex() {
        return selectedItemIndex;
    }
}
