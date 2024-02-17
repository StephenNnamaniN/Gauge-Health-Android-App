package uk.ac.tees.a0278818.gaugehealth.conditionsModels;

import java.util.HashMap;
import java.util.Map;
public class ItemListElement {
    private String type;
    private Integer position;
    private Item item;
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Integer getPosition() {
        return position;
    }
    public void setPosition(Integer position) {
        this.position = position;
    }
    public Item getItem() {
        return item;
    }
    public void setItem(Item item) {
        this.item = item;
    }
}
