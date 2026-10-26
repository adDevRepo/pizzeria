package fr.tp.pizzeria;

import fr.tp.pizzeria.model.Pizza;
import fr.tp.pizzeria.service.PizzeriaService;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        PizzeriaService ps = new PizzeriaService();
        ps.config("nom=Pizzeria Da Mario");

        List<Pizza> l = new ArrayList<>();
        l.add(ps.creer("marg", "napo", "M"));
        l.add(ps.creer("4fro", "ny", "L", true, false));
        l.add(ps.creer("vege", "napo", "S", false, true));
        l.add(ps.pizzaDuJour());
        ps.commande(l, true, true);

        List<Pizza> l2 = new ArrayList<>();
        l2.add(ps.creer("regina", "ny", "M"));
        ps.commande(l2, false, false);

        System.out.println("Regina vendues : " + ps.nbVendues("regina"));
        System.out.println("Chiffre d'affaires : " + ps.ca() + " EUR");
    }
}
