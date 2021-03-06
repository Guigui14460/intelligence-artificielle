package datamining;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
    private final List<Map<Variable, Object>> instances;

    /**
     * L'ensemble des variables possibles du problème.
     */
    private final Set<Variable> variables;

    /**
     * Constructeur par défaut.
     * 
     * @param variables l'ensemble des variables possibles du problème
     */
    public Database(final Set<Variable> variables) {
        this.variables = variables;
        this.instances = new ArrayList<>();
    }

    /**
     * Ajoute une instance à la base de données.
     * 
     * @param instance instance à ajouter
     */
    public void add(Map<Variable, Object> instance) {
        this.instances.add(instance);
    }

    /**
     * Transforme les instances en items. Map de variable ayant chacune comme valeur
     * une autre map. Cette autre map contient comme clé des objets (correspondant à
     * toutes les valeurs du domaine de la variable) et comme valeur une variable
     * booléenne si elle apparaît dans une instance sinon {@code null}.
     * 
     * @return table transformée en une map d'items
     */
    public Map<Variable, Map<Object, BooleanVariable>> itemTable() {
        Map<Variable, Map<Object, BooleanVariable>> map = new HashMap<>();
        for (Variable variable : this.variables) {
            Map<Object, BooleanVariable> map2 = new HashMap<>(); // sous-dictionnaire
            for (Object value : variable.getDomain()) {
                if (value instanceof Boolean) {
                    BooleanVariable boolVar = new BooleanVariable(variable.getName());
                    if (value.equals(false)) {
                        map2.put(value, null);
                    } else {
                        map2.put(value, boolVar);
                    }
                } else {
                    map2.put(value, new BooleanVariable(variable.getName() + " pour " + value)); // avoir des variables
                    // différentes liées à leur
                    // nom et à leur valeur
                }
            }
            map.put(variable, map2);
        }
        return map;
    }

    /**
     * Propositionalise la base en une base transactionnelle (reformulation).
     * 
     * @return base de données transactionnelles
     */
    public BooleanDatabase propositionalize() {
        // on récupère les variables booléennes pour créer la base de données
        // transactionnelles
        Set<BooleanVariable> booleanVariables = new HashSet<>();
        Map<Variable, Map<Object, BooleanVariable>> items = this.itemTable();
        for (Map.Entry<Variable, Map<Object, BooleanVariable>> entry : items.entrySet()) {
            for (Map.Entry<Object, BooleanVariable> subEntry : entry.getValue().entrySet()) {
                if (subEntry.getValue() != null) {
                    booleanVariables.add(subEntry.getValue());
                }
            }
        }

        // on crée toutes les transactions associées aux instanciations
        BooleanDatabase booleanDatabase = new BooleanDatabase(booleanVariables);
        for (Map<Variable, Object> instance : this.instances) {
            Set<BooleanVariable> booleanTransaction = new HashSet<>();
            for (Map.Entry<Variable, Object> entry : instance.entrySet()) {
                BooleanVariable booleanVariable = items.get(entry.getKey()).get(entry.getValue());
                if (booleanVariable != null) {
                    booleanTransaction.add(booleanVariable);
                }
            }
            booleanDatabase.add(booleanTransaction);
        }
        return booleanDatabase;
    }

    /**
     * Récupère l'ensemble des variables utilisées dans le problème.
     * 
     * @return ensemble de variables
     * @see #variables
     */
    public Set<Variable> getVariables() {
        return this.variables;
    }

    /**
     * Récupère la liste des toutes les instances stockées dans la base de données.
     * 
     * @return liste d'instances
     * @see #instances
     */
    public List<Map<Variable, Object>> getInstances() {
        return this.instances;
    }
}
