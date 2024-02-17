package uk.ac.tees.a0278818.gaugehealth.data;

import java.util.List;

import uk.ac.tees.a0278818.gaugehealth.conditionsModels.Item;

public abstract class AsyncResponse {
    public abstract void processItem(List<Item> items);
}
