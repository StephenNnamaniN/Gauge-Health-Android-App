package uk.ac.tees.a0278818.gaugehealth.conditionsModels;

import android.text.SegmentFinder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class ItemViewModel extends ViewModel {
    private final MutableLiveData<Item> selectedItem = new MutableLiveData<>();
    private final MutableLiveData<List<Item>> selectedItems = new MutableLiveData<>();

    public void setSelectedItem(Item item) {
        selectedItem.setValue(item);
    }
    public LiveData<Item> getSelectedItem(){
        return selectedItem;
    }
    public void setSelectedItems(List<Item> items){
        selectedItems.setValue(items);
    }
    public LiveData<List<Item>> getItems(){
        return selectedItems;
    }
}
