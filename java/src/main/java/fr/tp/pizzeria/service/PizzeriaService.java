package fr.tp.pizzeria.service;

import fr.tp.pizzeria.model.Commande;
import fr.tp.pizzeria.model.Pizza;
import fr.tp.pizzeria.model.StylePizza;
import fr.tp.pizzeria.model.TaillePizza;
import fr.tp.pizzeria.model.TypePizza;
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
        pdj = new Pizza.Builder()
                .nom("Pizza du jour")
                .type("regina")
                .taille("L")
                .style("napo")
                .pate("fine")
                .sauce("tomate San Marzano")
                .fromage("mozzarella di bufala")
                .garnitures(g)
                .extraFromage(true)
                .sansGluten(false)
                .prix(12.0)
                .build();
    }

    public Pizza creer(String t, String s, String tl) {
        return creer(t, s, tl, false, false);
    }

    // Fabrique une pizza selon son type et son style, puis lui attribue son prix.
    public Pizza creer(String t, String s, String tl, boolean ef, boolean sg) {
        if (ConfigRepository.params.get("debug").equals("true")) {
            System.out.println("creer " + t + " " + s + " " + tl);
        }

        TypePizza type = TypePizza.depuisCode(t);
        StylePizza style = StylePizza.depuisCode(s);
        if (type == null || style == null) {
            return null;
        }

        // L'enum du type porte le nom de la pizza et sa recette de base.
        Pizza p = new Pizza.Builder()
                .nom(type.getNom())
                .type(type.getCode())
                .taille(tl)
                .style(style.getCode())
                .pate(style.getPate())
                .sauce(style.getSauce())
                .fromage(style.getFromage(type))
                .garnitures(new ArrayList<>(type.getGarnitures()))
                .extraFromage(ef)
                .sansGluten(sg)
                .prix(0)
                .build();
        p.prix = calc(p);
        return p;
    }

    // Reprend la pizza du jour comme modèle et en renvoie une nouvelle instance (prototype).
    public Pizza pizzaDuJour() {
        Pizza x = new Pizza(pdj.nom, pdj.type, pdj.taille, pdj.style, pdj.pate, pdj.sauce,
                pdj.fromage, new ArrayList<>(pdj.garnitures), pdj.extraFromage, pdj.sansGluten, pdj.prix);
        return x;
    }

    // Calcule le prix à partir du type, de la taille et des suppléments configurés.
    public double calc(Pizza p) {
        // Les enums reconnaissent les valeurs prévues ; les valeurs inconnues
        // gardent le comportement historique des codes reçus en texte.
        TypePizza type = TypePizza.depuisCode(p.type);
        StylePizza style = StylePizza.depuisCode(p.style);
        TaillePizza taille = TaillePizza.depuisCode(p.taille);

        String codeType = type == null ? p.type : type.getCode();
        String codeTaille = taille == null ? p.taille : taille.getCode();

        double prix = Double.parseDouble(ConfigRepository.params.getOrDefault("prix." + codeType, "0"));
        double coefficientTaille = Double.parseDouble(
                ConfigRepository.params.getOrDefault("taille." + codeTaille, "1.0"));
        prix *= coefficientTaille;

        if (style == StylePizza.NEW_YORKAISE) {
            prix += Double.parseDouble(ConfigRepository.params.get("supplement.style.ny"));
        }
        if (p.extraFromage) {
            prix += Double.parseDouble(ConfigRepository.params.get("supplement.fromage"));
        }
        if (p.sansGluten) {
            prix += Double.parseDouble(ConfigRepository.params.get("supplement.sansGluten"));
        }

        return Math.round(prix * 100) / 100.0;
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

            // Les paramètres de prix acceptent des nombres, comme la TVA.
            if (tab[0].startsWith("prix.") || tab[0].startsWith("taille.")
                    || tab[0].startsWith("supplement.")) {
                if (ConfigRepository.params.containsKey(tab[0])) {
                    Double.parseDouble(tab[1]);
                    ConfigRepository.params.put(tab[0], tab[1]);
                }
            }
        } catch (Exception e) {
        }
    }
}
