package datamining;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import representation.BooleanVariable;
import representation.Variable;

/**
 * Classe représentant une base de données non transactionnelle.
 */
public class Database {
    /**
     * Liste d'instance.
     */
    private List<Map<Variable, Object>> instances;

    /**
     * L'ensemble des variables possibles du problème.
     */
    private Set<Variable> variables;

    /**
     * Constructeur par défaut.
     * @param variables l'ensemble des variables possibles du problème
     */
    public Database(Set<Variable> variables){
        this.variables = variables;
        this.instances = new LinkedList<>();
    }

    /**
     * Ajoute une instance à la base de donnéeS.
     * @param instance instance à ajouter
     */
    public void add(Map<Variable, Object> instance){
        this.instances.add(instance);
    }

    /**
     * Transforme les instances en items.
     * Map de variable ayant chacune comme valeur une autre map.
     * Cette autre map contient comme clé des objets (correspondant à toutes les valeurs du domaine de la variable)
     * et comme valeur une variable booléenne si elle apparaît dans une instance sinon {@code null}.
     * @return table transformée en une map d'items
     */
    public Map<Variable, Map<Object, BooleanVariable>> itemTable(){
        Map<Variable, Map<Object, BooleanVariable>> map = new HashMap<>();
        for(Variable variable: this.variables){
            Map<Object, BooleanVariable> map2 = new HashMap<>();
            for(Object value: variable.getDomain()){
                // on regarde dans chaque instance
                for(Map<Variable, Object> instance: this.instances){
                    if(instance.containsKey(variable)){ // si la variable est dans l'instance
                        if(instance.get(variable).equals(value)){ // si l'objet associé à la variable est le même qu'itéré
                            map2.put(value, new BooleanVariable(variable.getName())); // on créé une variable booléenne pour représenter la précense
                        } else {
                            map2.put(value, null); // représente l'absence de l'item dans l'instance
                        }
                    }
                }
            }
            map.put(variable, map2);
        }
        return map;
    }

    /**
     * Propositionalise la base en une base transactionnelle (reformulation).
     * @return base de données transactionnelles
     */
    public BooleanDatabase propositionalize(){
        Set<BooleanVariable> booleanVariables = new HashSet<>();

        Map<Variable, Map<Object, BooleanVariable>> items = this.itemTable();

        BooleanDatabase booleanDatabase = new BooleanDatabase(booleanVariables);
        for(int i = 0; i < this.instances.size(); i++){
            // ajouter les instances dans la base de données transactionnelle
        }
        return booleanDatabase;
    }

    /**
     * Récupère l'ensemble des variables utilisées dans le problème.
     * @return ensemble de variables
     * @see #variables
     */
    public Set<Variable> getVariables(){
        return this.variables;
    }

    /**
     * Récupère la liste des toutes les instances stockées dans la base de données.
     * @return liste d'instances
     * @see #instances
     */
    public List<Map<Variable, Object>> getInstances(){
        return this.instances;
    }
}
