package datamining;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import representation.BooleanVariable;

public class Apriori extends AbstractItemsetMiner {
    public Apriori(BooleanDatabase database) {
        super(database);
    }

    @Override
    public Set<Itemset> extract(float minimalFrequency) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public Set<Itemset> frequentSingletons(float minimalFrequency){
        Set<Itemset> itemsets = new HashSet<>();
        for(BooleanVariable item: this.database.getItems()){
            SortedSet<BooleanVariable> itemset = new TreeSet<>(AbstractItemsetMiner.COMPARATOR);
            itemset.add(item);
            float frequency = this.frequency(itemset);
            if(frequency >= minimalFrequency){
                itemsets.add(new Itemset(itemset, frequency));
            }
        }
        return itemsets;
    }

    public static SortedSet<BooleanVariable> combine(SortedSet<BooleanVariable> set1, SortedSet<BooleanVariable> set2){
        if(set1.size() != set2.size()){
            return null;
        }
        if(set1.size() == 0){
            return null;
        }
        int k = set1.size();
        Iterator<BooleanVariable> set1Iterator = set1.iterator();
        Iterator<BooleanVariable> set2Iterator = set2.iterator();
        for(int i = 0; i < k-1; i++){
            if(!set1Iterator.next().equals(set2Iterator.next())){
                return null;
            }
        }
        if(set1Iterator.next().equals(set2Iterator.next())){
            return null;
        }
        SortedSet<BooleanVariable> toReturn = new TreeSet<>(AbstractItemsetMiner.COMPARATOR);
        for(BooleanVariable variable: set1){
            toReturn.add(variable);
        }
        for(BooleanVariable variable: set2){
            toReturn.add(variable);
        }
        return toReturn;
    }

    public static boolean allSubsetsFrequent(Set<BooleanVariable> items, Collection<SortedSet<BooleanVariable>> itemsCollection){
        // TODO
        return false;
    }
}
