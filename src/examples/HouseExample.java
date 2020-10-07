package examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private int width;

    /**
     * Longueur de la maison.
     */
    private int length;

    /**
     * Ensemble de pièces d'eau.
     */
    private Set<String> waterRooms;

    /**
     * Ensemble d'autres pièces autre que des pièces d'eau.
     */
    private Set<String> otherRooms;

    /**
     * Liste de variables.
     */
    private List<Variable> variables;

    /**
     * Liste de contraintes à satisfaire.
     */
    private List<Constraint> constraints;

    /**
     * Constructeur par défaut.
     * @param width largeur de la maison
     * @param length longueur de la maison
     * @param waterRooms ensemble de pièce d'eau
     * @param otherRooms ensemble d'autre pièces
     */
    public HouseExample(int width, int length, Set<String> waterRooms, Set<String> otherRooms) {
        this.width = width;
        this.length = length;
        this.waterRooms = waterRooms;
        this.otherRooms = otherRooms;
        this.variables = new ArrayList<>();
        this.constraints = new ArrayList<>();
    }

    /**
     * Ajoute des variables à la liste de variables.
     * @param vars variables à ajouter (une ou plus)
     */
    public void addVariables(Variable... vars){
        this.variables.addAll(Arrays.asList(vars));
    }

    /**
     * Enlève des variables de la liste de variables.
     * @param vars variables à enlever (une ou plus)
     */
    public void removeVariables(Variable... vars){
        this.variables.removeAll(Arrays.asList(vars));
    }

    /**
     * Ajoute des contraintes à la liste de contraintes.
     * @param cons contraintes à ajouter (une ou plus)
     */
    public void addConstraints(Constraint... cons){
        this.constraints.addAll(Arrays.asList(cons));
    }

    /**
     * Enlève des contraintes de la liste de contraintes.
     * @param cons contraintes à enlever (une ou plus)
     */
    public void removeConstraints(Constraint... cons){
        this.constraints.removeAll(Arrays.asList(cons));
    }

    /**
     * Retourne la liste des variables.
     * @return liste des variables
     */
    public List<Variable> getVariables(){
        return this.variables;
    }

    /**
     * Retourne la liste des contraintes à satisfaire.
     * @return liste des contraintes à satisfaire
     */
    public List<Constraint> getConstraints(){
        return this.constraints;
    }

    /**
     * Retourne la largeur de la maison.
     * @return largeur de la maison
     */
    public int getWidth(){
        return this.width;
    }

    /**
     * Retourne la longueur de la maison.
     * @return longueur de la maison
     */
    public int getLength(){
        return this.length;
    }

    /**
     * Retourne un ensemble de pièces d'eau.
     * @return ensemble de pièces d'eau
     */
    public Set<String> getWaterRooms(){
        return this.waterRooms;
    }

    /**
     * Retourne un ensemble d'autres pièces autre que des pièces d'eau.
     * @return ensemble d'autres pièces autre que des pièces d'eau
     */
    public Set<String> getOtherRooms(){
        return this.otherRooms;
    }
}
