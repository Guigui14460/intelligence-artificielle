package datamining;

import java.util.Comparator;
import java.util.Set;

import representation.BooleanVariable;

public abstract class AbstractItemsetMiner implements ItemsetMiner {
    public static final Comparator<BooleanVariable> COMPARATOR = (var1, var2) -> var1.getName().compareTo(var2.getName());
    
    protected BooleanDatabase database;
    
    public AbstractItemsetMiner(BooleanDatabase database){
        this.database = database;
    }

    public float frequency(Set<BooleanVariable> itemset){
        float numberOfOccurence = 0;
        for(Set<BooleanVariable> iteratedItemset: this.database.getTransactions()){
            if(iteratedItemset.containsAll(itemset)){
                numberOfOccurence++;
            }
        }
        return numberOfOccurence / this.database.getTransactions().size();
    }

    public BooleanDatabase getDatabase(){
        return this.database;
    }
}