package datamining;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;

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
        System.out.println("----------------------------------------");
        System.out.println(this.database.getTransactions());
        System.out.println(minimalFrequency);
        Set<Itemset> itemsets = new HashSet<>();
        for(Set<BooleanVariable> transaction: this.database.getTransactions()){
            float frequency = this.frequency(transaction);
            System.out.println(transaction + " : " + frequency);
            if(frequency >= minimalFrequency){
                itemsets.add(new Itemset(transaction, frequency));
            }
        }
        return itemsets;
    }

    public static SortedSet<BooleanVariable> combine(SortedSet<BooleanVariable> set1, SortedSet<BooleanVariable> set2){
        if(set1.size() != set2.size()){
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
        SortedSet<BooleanVariable> toReturn = set1.headSet(set1.last());
        toReturn.add(set1.last());
        toReturn.add(set2.last());
        return toReturn;
    }

    public static boolean allSubsetsFrequent(Set<BooleanVariable> items, Collection<SortedSet<BooleanVariable>> itemsCollection){
        // TODO
        return false;
    }
}
