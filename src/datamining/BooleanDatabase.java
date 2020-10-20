package datamining;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import representation.BooleanVariable;

public class BooleanDatabase {
    private Set<BooleanVariable> itemset;
    private List<Set<BooleanVariable>> transactions;

    public BooleanDatabase(Set<BooleanVariable> itemset){
        this.itemset = itemset;
        this.transactions = new ArrayList<>();
    }

    public void add(Set<BooleanVariable> transaction){
        this.transactions.add(transaction);
    }

    public Set<BooleanVariable> getItems(){
        return this.itemset;
    }

    public List<Set<BooleanVariable>> getTransactions(){
        return this.transactions;
    }
}
