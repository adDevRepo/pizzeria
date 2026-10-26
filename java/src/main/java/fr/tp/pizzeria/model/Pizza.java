package fr.tp.pizzeria.model;

import java.util.List;

public class Pizza {
    public String nom;
    public String type;
    public String taille;
    public String style;
    public String pate;
    public String sauce;
    public String fromage;
    public List<String> garnitures;
    public boolean extraFromage;
    public boolean sansGluten;
    public double prix;

    public Pizza(String nom, String type, String taille, String style, String pate, String sauce,
                 String fromage, List<String> garnitures, boolean extraFromage, boolean sansGluten,
                 double prix) {
        this.nom = nom;
        this.type = type;
        this.taille = taille;
        this.style = style;
        this.pate = pate;
        this.sauce = sauce;
        this.fromage = fromage;
        this.garnitures = garnitures;
        this.extraFromage = extraFromage;
        this.sansGluten = sansGluten;
        this.prix = prix;
    }
}
