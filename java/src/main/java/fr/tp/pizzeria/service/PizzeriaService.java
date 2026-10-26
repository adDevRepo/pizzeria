package fr.tp.pizzeria.service;

import fr.tp.pizzeria.model.Commande;
import fr.tp.pizzeria.model.Pizza;
import fr.tp.pizzeria.repository.ConfigRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Service de la pizzeria.
 * Gère les pizzas.
 */
public class PizzeriaService {

    private Pizza pdj;
    private final CommandeService commandeService = new CommandeService();

    public PizzeriaService() {
        // on prépare la pizza du jour
        List<String> g = new ArrayList<>();
        g.add("jambon");
        g.add("champignons");
        g.add("artichauts");
        pdj = new Pizza("Pizza du jour", "regina", "L", "napo", "fine", "tomate San Marzano",
                "mozzarella di bufala", g, true, false, 12.0);
    }

    public Pizza creer(String t, String s, String tl) {
        return creer(t, s, tl, false, false);
    }

    // Fabrique une pizza selon son type et son style, puis lui attribue son prix.
    public Pizza creer(String t, String s, String tl, boolean ef, boolean sg) {
        Pizza p = null;
        List<String> g = new ArrayList<>();
        if (t.equals("marg")) {
            g.add("basilic");
            if (s.equals("napo")) {
                p = new Pizza("Margherita", t, tl, s, "fine", "tomate San Marzano", "mozzarella di bufala", g, ef, sg, 0);
            } else if (s.equals("ny")) {
                p = new Pizza("Margherita", t, tl, s, "épaisse", "tomate origan", "mozzarella râpée", g, ef, sg, 0);
            }
        } else if (t.equals("4fro")) {
            g.add("gorgonzola");
            g.add("chèvre");
            g.add("emmental");
            if (s.equals("napo")) {
                p = new Pizza("Quatre fromages", t, tl, s, "fine", "tomate San Marzano", "mozzarella di bufala", g, ef, sg, 0);
            } else if (s.equals("ny")) {
                p = new Pizza("Quatre fromages", t, tl, s, "épaisse", "tomate origan", "mozzarella râpée", g, ef, sg, 0);
            }
        } else if (t.equals("regina")) {
            g.add("jambon");
            g.add("champignons");
            if (s.equals("napo")) {
                p = new Pizza("Regina", t, tl, s, "fine", "tomate San Marzano", "mozzarella di bufala", g, ef, sg, 0);
            } else if (s.equals("ny")) {
                p = new Pizza("Regina", t, tl, s, "épaisse", "tomate origan", "mozzarella râpée", g, ef, sg, 0);
            }
        } else if (t.equals("vege")) {
            g.add("poivrons");
            g.add("champignons");
            g.add("oignons");
            g.add("olives");
            if (s.equals("napo")) {
                p = new Pizza("Végétarienne", t, tl, s, "fine", "tomate San Marzano", "mozzarella di bufala", g, ef, sg, 0);
            } else if (s.equals("ny")) {
                p = new Pizza("Végétarienne", t, tl, s, "épaisse", "tomate origan", "mozzarella di bufala", g, ef, sg, 0);
            }
        }
        if (p != null) {
            p.prix = calc(p);
        }
        if (ConfigRepository.params.get("debug").equals("true")) System.out.println("creer " + t + " " + s + " " + tl);
        return p;
    }

    // Reprend la pizza du jour comme modèle et en renvoie une nouvelle instance (prototype).
    public Pizza pizzaDuJour() {
        Pizza x = new Pizza(pdj.nom, pdj.type, pdj.taille, pdj.style, pdj.pate, pdj.sauce,
                pdj.fromage, pdj.garnitures, pdj.extraFromage, pdj.sansGluten, pdj.prix);
        return x;
    }

    // calcule le prix
    public double calc(Pizza p) {
        double x = 0;
        if (p.type.equals("marg")) x = 8;
        if (p.type.equals("4fro")) x = 11;
        if (p.type.equals("regina")) x = 10;
        if (p.type.equals("vege")) x = 9.5;

        // taille
        if (p.taille.equals("S")) {
            x = x * 0.8;
        } else if (p.taille.equals("L")) {
            x = x * 1.3;
        } else {
            x = x * 1;
        }

        // pâte épaisse plus chère
        if (p.style.equals("ny")) x = x + 1;

        if (p.extraFromage == true) x = x + 1.5;
        if (p.sansGluten == true) x = x + 2;

        x = Math.round(x * 100) / 100.0;
        return x;
    }

    // ancienne méthode de calcul (garder au cas où)
    private double ancienCalc(Pizza p) {
        double x = 0;
        if (p.type.equals("marg")) x = 7.5;
        if (p.type.equals("4fro")) x = 10;
        if (p.type.equals("regina")) x = 9;
        // if (p.taille.equals("L")) x = x * 1.25;
        return x;
    }

    // Garde l'ancien point d'entrée : le traitement est maintenant dans CommandeService.
    public String commande(List<Pizza> l, boolean emp, boolean fid) {
        return commandeService.commande(l, emp, fid);
    }

    // nombre de pizzas vendues
    public int nbVendues(String t) {
        return commandeService.nbVendues(t);
    }

    // retourne une commande
    public Commande get(int n) {
        return commandeService.get(n);
    }

    // retourne toutes les commandes
    public List<Commande> historique() {
        return commandeService.historique();
    }

    // chiffre d'affaires
    public double ca() {
        return commandeService.ca();
    }

    // charge une ligne de config
    public void config(String l) {
        try {
            String[] tab = l.split("=");
            if (tab[0].equals("tva")) {
                Double.parseDouble(tab[1]);
                new ConfigRepository().set("tva", tab[1]);
            }
            if (tab[0].equals("nom")) ConfigRepository.params.put("nom", tab[1]);
        } catch (Exception e) {
        }
    }
}
