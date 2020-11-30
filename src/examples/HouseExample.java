package examples;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import representation.Constraint;
import representation.Variable;

/**
 * Example avec la maison.
 */
public class HouseExample {
    /**
     * Largeur de la maison.
     */
    private final int width;

    /**
     * Longueur de la maison.
     */
    private final int length;

    /**
     * Ensemble de pièces d'eau.
     */
    private final Set<String> wetRooms;

    /**
     * Ensemble d'autres pièces autre que des pièces d'eau.
     */
    private final Set<String> dryRooms;

    /**
     * Ensemble de variables.
     */
    private final Set<Variable> variables;

    /**
     * Ensemble de contraintes à satisfaire.
     */
    private final Set<Constraint> constraints;

    /**
     * Constructeur par défaut.
     * 
     * @param width    largeur de la maison
     * @param length   longueur de la maison
     * @param wetRooms ensemble de pièce d'eau
     * @param dryRooms ensemble d'autre pièces
     */
    public HouseExample(int width, int length, Set<String> wetRooms, Set<String> dryRooms) {
        this.width = width;
        this.length = length;
        this.wetRooms = wetRooms;
        this.dryRooms = dryRooms;
        this.variables = new HashSet<>();
        this.constraints = new HashSet<>();
    }

    /**
     * Ajoute des variables à l'ensemble de variables.
     * 
     * @param vars variables à ajouter (une ou plus)
     */
    public void addVariables(Variable... vars) {
        this.variables.addAll(Arrays.asList(vars));
    }

    /**
     * Ajoute des variables à l'ensemble de variables.
     * 
     * @param vars collection de variables à ajouter (une ou plus)
     */
    public void addVariables(Collection<Variable> vars) {
        this.variables.addAll(vars);
    }

    /**
     * Enlève des variables de l'ensemble de variables.
     * 
     * @param vars variables à enlever (une ou plus)
     */
    public void removeVariables(Variable... vars) {
        this.variables.removeAll(Arrays.asList(vars));
    }

    /**
     * Enlève des variables de l'ensemble de variables.
     * 
     * @param vars collection de variables à enlever (une ou plus)
     */
    public void removeVariables(Collection<Variable> vars) {
        this.variables.removeAll(vars);
    }

    /**
     * Ajoute des contraintes à l'ensemble de contraintes.
     * 
     * @param cons contraintes à ajouter (une ou plus)
     */
    public void addConstraints(Constraint... cons) {
        this.constraints.addAll(Arrays.asList(cons));
    }

    /**
     * Ajoute des contraintes à l'ensemble de contraintes.
     * 
     * @param cons collection de contraintes à ajouter (une ou plus)
     */
    public void addConstraints(Collection<Constraint> cons) {
        this.constraints.addAll(cons);
    }

    /**
     * Enlève des contraintes de l'ensemble de contraintes.
     * 
     * @param cons contraintes à enlever (une ou plus)
     */
    public void removeConstraints(Constraint... cons) {
        this.constraints.removeAll(Arrays.asList(cons));
    }

    /**
     * Enlève des contraintes de l'ensemble de contraintes.
     * 
     * @param cons collection de contraintes à enlever (une ou plus)
     */
    public void removeConstraints(Collection<Constraint> cons) {
        this.constraints.removeAll(cons);
    }

    /**
     * Retourne la ensemble des variables.
     * 
     * @return ensemble des variables
     */
    public Set<Variable> getVariables() {
        return this.variables;
    }

    /**
     * Retourne la ensemble des contraintes à satisfaire.
     * 
     * @return ensemble des contraintes à satisfaire
     */
    public Set<Constraint> getConstraints() {
        return this.constraints;
    }

    /**
     * Retourne la largeur de la maison.
     * 
     * @return largeur de la maison
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Retourne la longueur de la maison.
     * 
     * @return longueur de la maison
     */
    public int getLength() {
        return this.length;
    }

    /**
     * Retourne un ensemble de pièces d'eau.
     * 
     * @return ensemble de pièces d'eau
     */
    public Set<String> getWetRooms() {
        return this.wetRooms;
    }

    /**
     * Retourne un ensemble d'autres pièces autre que des pièces d'eau.
     * 
     * @return ensemble d'autres pièces autre que des pièces d'eau
     */
    public Set<String> getDryRooms() {
        return this.dryRooms;
    }
}
