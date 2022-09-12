/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.workers;

import app.beans.Personne;
import java.util.List;

/**
 *
 * @author Fejzaje
 */
public class PersonneManager {
    
    private List<Personne> listePersonnes;
    private int index = 0;
    
    public Personne courantPersonne(){
        if (index > listePersonnes.size()) {
            index--;
        }
        return listePersonnes.get(index);
    }
    
    public Personne debutPersonne(){
        index = 0;
        return listePersonnes.get(index);
    }
    
    public Personne finPersonne(){
        index = listePersonnes.size()-1;
        return listePersonnes.get(index);
    }
    
    public Personne precedentPersonne(){
        if (index > 0) {
            index = index - 1;
        }
        
        return listePersonnes.get(index);
    }
    
    public Personne setPersonnes(List<Personne> pers){
        this.listePersonnes = pers;
        return courantPersonne();
    }
    
    public Personne suivantPersonne(){
        if (index < listePersonnes.size()-1) {
            index += 1;
        }
        return listePersonnes.get(index);
    }
}
