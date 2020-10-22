package datamining;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import representation.BooleanVariable;

/**
 * Classe représentant un extracteur de données fonctionnant sur le principe de l'algorithme 'apriori'.
 */
public class Apriori extends AbstractItemsetMiner {
    /**
     * Contructeur par défaut.
     * @param database base de données à utiliser
     */
    public Apriori(BooleanDatabase database) {
        super(database);
    }

    @Override
    public Set<Itemset> extract(float minimalFrequency) {
        // initialisation de collections utiles à l'algorithme apriori
        Set<Itemset> results = new HashSet<>();
        List<SortedSet<BooleanVariable>> listOfDiscoveredItemsets = new ArrayList<>(); // stocke les ensembles d'items fréquents découverts au niveau k
        
        // on récupère les ensembles à 1 éléments les plus fréquents
        Set<Itemset> firstLevel = this.frequentSingletons(minimalFrequency);
        results.addAll(firstLevel);
        // System.out.println(firstLevel);

        // on les ajoute à notre liste de motifs découverts
        SortedSet<BooleanVariable> lastFoundSortedSet = new TreeSet<>(AbstractItemsetMiner.COMPARATOR);
        for(Itemset itemset: firstLevel){
            lastFoundSortedSet.addAll(itemset.getItems());
        }
        listOfDiscoveredItemsets.add(lastFoundSortedSet);

        // System.out.println(results);

        // on regarde pour chaque motifs découverts
        int k = 0;
        while(k < listOfDiscoveredItemsets.size()){
            SortedSet<BooleanVariable> lastLevel = new TreeSet<>(listOfDiscoveredItemsets.get(k));
            for(BooleanVariable variable: this.database.getItems()){
                if(!lastLevel.contains(variable)){
                    lastLevel.add(variable); // on ajoute la variable itérée
                    if(Apriori.allSubsetsFrequent(lastLevel, listOfDiscoveredItemsets)){
                        SortedSet<BooleanVariable> set = new TreeSet<>(lastLevel);
                        listOfDiscoveredItemsets.add(set); // on l'ajoute à ceux découverts
                        Itemset itemset = new Itemset(set, this.frequency(set)); // on crée le motif avec sa fréquence dans la base de données
                        if(itemset.getFrequency() >= minimalFrequency){
                            results.add(itemset); // on l'ajoute aux résultats trouvés
                        }
                    }
                    lastLevel.remove(variable); // on la retire pour garder la même structure de base
                }
            }
            k++;
        }
        return results;
    }
    
    /**
     * Récupère un ensemble de motifs fréquemment trouvés dans la base de données transactionelle.
     * L'algorithme ne renvoie que les motifs ayant une fréquence supérieure où égale à la minimale.
     * @param minimalFrequency fréquence minimale pour la filtration
     * @return motifs fréquents dans la base de données
     */
    public Set<Itemset> frequentSingletons(float minimalFrequency){
        // System.out.println("--------------------------------");
        // System.out.println(this.database.getItems());
        // System.out.println(this.database.getTransactions());
        Set<Itemset> itemsets = new HashSet<>();
        for(BooleanVariable item: this.database.getItems()){
            // on itère chaque variable et on la converti en un ensemble ne contenant que la variable itérée
            SortedSet<BooleanVariable> itemset = new TreeSet<>(AbstractItemsetMiner.COMPARATOR);
            itemset.add(item);

            float frequency = this.frequency(itemset);
            if(frequency >= minimalFrequency){ // on ne garde que les motifs dépassant la fréquence minimale
                itemsets.add(new Itemset(itemset, frequency));
            }
        }
        // System.out.println("Result : " + itemsets);
        return itemsets;
    }

    /**
     * Combine deux ensembles triés (par le comparateur {@link AbstractItemsetMiner#COMPARATOR})
     * et un seul ensemble trié.
     * Les deux ensembles doivent avoir la même taille k et les k-1 premiers éléments doivent être les
     * mêmes dans les deux ensembles.
     * @param set1 premier ensemble trié de variables
     * @param set2 second ensemble trié de variables
     * @return ensemble combinant les deux ensembles donnés en argument
     */
    public static SortedSet<BooleanVariable> combine(SortedSet<BooleanVariable> set1, SortedSet<BooleanVariable> set2){
        // vérification qu'ils aient la même taille k
        if(set1.size() != set2.size()){
            return null;
        }
        // si ils sont vides, on les fusionnent pas
        if(set1.size() == 0){
            return null;
        }
        // vérification que les k-1 premiers éléments sont les mêmes
        int k = set1.size();
        Iterator<BooleanVariable> set1Iterator = set1.iterator();
        Iterator<BooleanVariable> set2Iterator = set2.iterator();
        for(int i = 0; i < k-1; i++){
            if(!set1Iterator.next().equals(set2Iterator.next())){
                return null;
            }
        }
        // on regarde le dernier élément (si égaux, pas de fusion car les 2 ensembles sont les mêmes)
        if(set1Iterator.next().equals(set2Iterator.next())){
            return null;
        }
        // on fusionne les 2
        SortedSet<BooleanVariable> toReturn = new TreeSet<>(AbstractItemsetMiner.COMPARATOR);
        for(BooleanVariable variable: set1){
            toReturn.add(variable);
        }
        for(BooleanVariable variable: set2){
            toReturn.add(variable);
        }
        return toReturn;
    }

    /**
     * Vérifie que tous les sous-ensembles de variables d'un ensemble de variable donné sont contenus dans une collection.
     * Cette méthode utilise la propriété d'antimonotonie de la fréquence assurant que les sous-ensembles
     * plus petits sont fréquents.
     * @param items ensemble de variables
     * @param itemsCollection collection d'ensemble de variables
     * @return booléen représentant le fait que tous les sous-ensembles (de taille k-1) sont contenus dans la collection
     */
    public static boolean allSubsetsFrequent(Set<BooleanVariable> items, Collection<SortedSet<BooleanVariable>> itemsCollection){
        boolean allFrequent = true;
        for(BooleanVariable variable: items){
            Set<BooleanVariable> items2 = new HashSet<>(items); // copie pour éviter une ConcurrentModificationException
            items2.remove(variable);
            allFrequent = allFrequent && itemsCollection.contains(items2);
        }
        return allFrequent;
    }
}
