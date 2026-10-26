package fr.tp.pizzeria.repository;

import java.util.HashMap;
import java.util.Map;

// Stocke les paramètres de la pizzeria
public class ConfigRepository {

    public static Map<String, String> params = new HashMap<>();

    static {
        params.put("nom", "Pizzeria Da Mario");
        params.put("tva", "0.10");
        params.put("debug", "false");

        // Valeurs utilisées par le calcul du prix d'une pizza.
        params.put("prix.marg", "8.0");
        params.put("prix.4fro", "11.0");
        params.put("prix.regina", "10.0");
        params.put("prix.vege", "9.5");
        params.put("taille.S", "0.8");
        params.put("taille.M", "1.0");
        params.put("taille.L", "1.3");
        params.put("supplement.style.ny", "1.0");
        params.put("supplement.fromage", "1.5");
        params.put("supplement.sansGluten", "2.0");
    }

    // Suppression getter inutile

    public void set(String k, String v) {
        params.put(k, v);
    }
}
