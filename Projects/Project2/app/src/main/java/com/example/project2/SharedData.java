package com.example.project2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedData extends ViewModel {

    private final MutableLiveData<Integer> selectedItem = new MutableLiveData<>();
    private final MutableLiveData<Integer> selectedItemIndex = new MutableLiveData<>();

    public void setSelectedItem(Integer item) {
        selectedItem.setValue(item);
    }

    public LiveData<Integer> getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItemIndex(Integer index) {
        selectedItemIndex.postValue(index);
    }

    public LiveData<Integer> getSelectedItemIndex() {
        return selectedItemIndex;
    }
}
