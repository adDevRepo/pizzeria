package fr.tp.pizzeria.model;

import java.util.List;

/** Les types de pizza proposés et leurs recettes de base. */
public enum TypePizza {
    MARGHERITA("marg", "Margherita", List.of("basilic")),
    QUATRE_FROMAGES("4fro", "Quatre fromages", List.of("gorgonzola", "chèvre", "emmental")),
    REGINA("regina", "Regina", List.of("jambon", "champignons")),
    VEGETARIENNE("vege", "Végétarienne", List.of("poivrons", "champignons", "oignons", "olives"));

    private final String code;
    private final String nom;
    private final List<String> garnitures;

    TypePizza(String code, String nom, List<String> garnitures) {
        this.code = code;
        this.nom = nom;
        this.garnitures = garnitures;
    }

    public String getCode() {
        return code;
    }

    public String getNom() {
        return nom;
    }

    public List<String> getGarnitures() {
        return garnitures;
    }

    public static TypePizza depuisCode(String code) {
        for (TypePizza type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
