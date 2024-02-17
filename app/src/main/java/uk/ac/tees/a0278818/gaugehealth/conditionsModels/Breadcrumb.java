package uk.ac.tees.a0278818.gaugehealth.conditionsModels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Breadcrumb {
    private String context;
    private String type;
    private List<ItemListElement> itemListElement = new ArrayList<ItemListElement>();
    public String getContext() {
        return context;
    }
    public void setContext(String context) {
        this.context = context;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public List<ItemListElement> getItemListElement() {
        return itemListElement;
    }
    public void setItemListElement(List<ItemListElement> itemListElement) {
        this.itemListElement = itemListElement;
    }
}
