package fr.tp.pizzeria.repository;

import fr.tp.pizzeria.model.Commande;
import fr.tp.pizzeria.model.Pizza;

import java.util.ArrayList;
import java.util.List;

// Repository des commandes (en mémoire)
public class CommandeRepository {

    public static List<Commande> data = new ArrayList<>();
    public static int cpt = 0;

    // retourne le numéro de la prochaine commande
    public int prochainNumero() {
        cpt++;
        return cpt;
    }

    public void save(Commande c) {
        data.add(c);
    }

    // retourne toutes les commandes
    public List<Commande> getAll() {
        return data;
    }

    public Commande findByNum(int n) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).num == n) {
                return data.get(i);
            }
        }
        return null;
    }

    // compte les pizzas vendues d'un type
    public int compter(String t) {
        int n = 0;
        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).pizzas.size(); j++) {
                if (data.get(i).pizzas.get(j).type.equals(t)) n++;
            }
        }
        return n;
    }

    // chiffre d'affaires TTC
    public double ca() {
        double x = 0;
        for (int i = 0; i < data.size(); i++) {
            x = x + data.get(i).ttc;
        }
        x = Math.round(x * 100) / 100.0;
        return x;
    }
}
