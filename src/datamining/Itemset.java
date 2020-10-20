package datamining;

import java.util.Set;

import representation.BooleanVariable;

public class Itemset {
    private Set<BooleanVariable> items;
    private float frequency;

    public Itemset(Set<BooleanVariable> items, float frequency){
        this.items = items;
        this.frequency = frequency;
    }

    public Set<BooleanVariable> getItems(){
        return this.items;
    }

    public float getFrequency(){
        return this.frequency;
    }
}
