package fr.tp.pizzeria.service;

import fr.tp.pizzeria.model.Commande;
import fr.tp.pizzeria.model.Pizza;
import fr.tp.pizzeria.repository.CommandeRepository;
import fr.tp.pizzeria.repository.ConfigRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Service dédié au traitement des commandes.
 * Prépare le ticket, applique les remises et enregistre la commande.
 */
public class CommandeService {

    // Passe une commande et calcule les montants à afficher sur le ticket.
    public String commande(List<Pizza> l, boolean emp, boolean fid) {
        CommandeRepository repo = new CommandeRepository();
        Commande c = new Commande();
        c.num = repo.prochainNumero();
        c.pizzas = new ArrayList<>();
        c.emp = emp;
        c.fid = fid;

        double total = 0;
        for (int i = 0; i < l.size(); i++) {
            Pizza p = l.get(i);
            if (p != null) {
                total += p.prix;
                c.pizzas.add(p);
            }
        }

        StringBuilder ticket = new StringBuilder();
        ticket.append("=== ").append(ConfigRepository.params.get("nom")).append(" ===\n");
        ticket.append("Commande n°").append(c.num).append("\n");
        for (Pizza p : c.pizzas) {
            ticket.append("- ").append(p.nom).append(" (").append(p.taille).append(", ")
                    .append(p.style).append(") : ")
                    .append(String.format(Locale.ROOT, "%.2f", p.prix)).append(" EUR\n");
        }

        // Le seuil fidélité se calcule avant la remise à emporter.
        double totalAvantRemise = total;
        if (fid) {
            if (totalAvantRemise >= 50) {
                total = total - total * 0.15;
                ticket.append("Remise fidélité : -15%\n");
            } else {
                total = total - total * 0.1;
                ticket.append("Remise fidélité : -10%\n");
            }
        }
        if (emp) {
            total = total - total * 0.05;
            ticket.append("Remise à emporter : -5%\n");
        }

        total = Math.round(total * 100) / 100.0;
        double tva = Math.round(total * Double.parseDouble(ConfigRepository.params.get("tva")) * 100) / 100.0;
        double ttc = Math.round((total + tva) * 100) / 100.0;

        c.ht = total;
        c.tva = tva;
        c.ttc = ttc;
        ticket.append("Total HT : ").append(String.format(Locale.ROOT, "%.2f", total)).append(" EUR\n");
        ticket.append("TVA : ").append(String.format(Locale.ROOT, "%.2f", tva)).append(" EUR\n");
        ticket.append("Total TTC : ").append(String.format(Locale.ROOT, "%.2f", ttc)).append(" EUR\n");
        c.ticket = ticket.toString();
        repo.save(c);

        System.out.println(c.ticket);
        return c.ticket;
    }

    // Retourne le nombre de pizzas vendues pour un type donné.
    public int nbVendues(String t) {
        return new CommandeRepository().compter(t);
    }

    // Recherche une commande grâce à son numéro.
    public Commande get(int n) {
        return new CommandeRepository().findByNum(n);
    }

    // Retourne l'historique dans l'ordre d'enregistrement.
    public List<Commande> historique() {
        return new CommandeRepository().getAll();
    }

    // Retourne le chiffre d'affaires TTC.
    public double ca() {
        return new CommandeRepository().ca();
    }
}
