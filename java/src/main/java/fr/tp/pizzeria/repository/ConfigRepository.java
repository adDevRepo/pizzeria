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
    }

    public String get(String k) {
        return params.get(k);
    }

    public void set(String k, String v) {
        params.put(k, v);
    }
}
