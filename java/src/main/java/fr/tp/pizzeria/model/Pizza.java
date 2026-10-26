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


    public static class Builder {
        private String nom;
        private String type;
        private String taille;
        private String style;
        private String pate;
        private String sauce;
        private String fromage;
        private List<String> garnitures;
        private boolean extraFromage;
        private boolean sansGluten;
        private double prix;

        public Builder nom(String nom) {
            this.nom = nom;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder taille(String taille) {
            this.taille = taille;
            return this;
        }

        public Builder style(String style) {
            this.style = style;
            return this;
        }

        public Builder pate(String pate) {
            this.pate = pate;
            return this;
        }

        public Builder sauce(String sauce) {
            this.sauce = sauce;
            return this;
        }

        public Builder fromage(String fromage) {
            this.fromage = fromage;
            return this;
        }

        public Builder garnitures(List<String> garnitures) {
            this.garnitures = garnitures;
            return this;
        }

        public Builder extraFromage(boolean extraFromage) {
            this.extraFromage = extraFromage;
            return this;
        }

        public Builder sansGluten(boolean sansGluten) {
            this.sansGluten = sansGluten;
            return this;
        }

        public Builder prix(double prix) {
            this.prix = prix;
            return this;
        }

        public Pizza build() {
            return new Pizza(nom, type, taille, style, pate, sauce, fromage,
                    garnitures, extraFromage, sansGluten, prix);
        }
    }
}
