package fr.tp.pizzeria.service;

import fr.tp.pizzeria.model.Commande;
import fr.tp.pizzeria.model.Pizza;
import fr.tp.pizzeria.repository.CommandeRepository;
import fr.tp.pizzeria.repository.ConfigRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de caractérisation de la couche service : ils figent le comportement ACTUEL.
 * Ils doivent rester verts pendant toute la refactorisation.
 * (Vous pourrez adapter les appels lorsque les signatures changeront,
 *  mais pas les valeurs attendues.)
 */
class PizzeriaServiceTest {

    private static final double DELTA = 0.001;

    private PizzeriaService ps;

    @BeforeEach
    void setUp() {
        // L'état global doit être remis à zéro avant chaque test
        ConfigRepository.params.put("nom", "Pizzeria Da Mario");
        ConfigRepository.params.put("tva", "0.10");
        ConfigRepository.params.put("debug", "false");
        CommandeRepository.data.clear();
        CommandeRepository.cpt = 0;
        ps = new PizzeriaService();
    }

    // ---------- Prix ----------

    @Test
    void margheritaMoyenneNapolitaine() {
        Pizza p = ps.creer("marg", "napo", "M");
        assertEquals("Margherita", p.nom);
        assertEquals(8.00, p.prix, DELTA);
    }

    @Test
    void quatreFromagesLargeNewYork() {
        Pizza p = ps.creer("4fro", "ny", "L");
        assertEquals("Quatre fromages", p.nom);
        assertEquals(15.30, p.prix, DELTA);
    }

    @Test
    void reginaSmallAvecSupplementFromage() {
        Pizza p = ps.creer("regina", "napo", "S", true, false);
        assertEquals(9.50, p.prix, DELTA);
        assertTrue(p.extraFromage);
    }

    @Test
    void vegetarienneSansGlutenNewYork() {
        Pizza p = ps.creer("vege", "ny", "M", false, true);
        assertEquals("Végétarienne", p.nom);
        assertEquals(12.50, p.prix, DELTA);
        assertTrue(p.sansGluten);
    }

    @Test
    void tailleInconnueEstTraiteeCommeMoyenne() {
        Pizza p = ps.creer("marg", "napo", "XXL");
        assertEquals(8.00, p.prix, DELTA);
    }

    @Test
    void typeInconnuRetourneNull() {
        assertNull(ps.creer("hawai", "napo", "M"));
    }

    @Test
    void styleInconnuRetourneNull() {
        assertNull(ps.creer("marg", "romaine", "M"));
    }

    // ---------- Ingrédients ----------

    @Test
    void ingredientsStyleNapolitain() {
        Pizza p = ps.creer("regina", "napo", "M");
        assertEquals("fine", p.pate);
        assertEquals("tomate San Marzano", p.sauce);
        assertEquals("mozzarella di bufala", p.fromage);
    }

    @Test
    void ingredientsStyleNewYork() {
        Pizza p = ps.creer("marg", "ny", "M");
        assertEquals("épaisse", p.pate);
        assertEquals("tomate origan", p.sauce);
        assertEquals("mozzarella râpée", p.fromage);
    }

    @Test
    void garnituresParType() {
        assertEquals(List.of("basilic"), ps.creer("marg", "napo", "M").garnitures);
        assertEquals(List.of("gorgonzola", "chèvre", "emmental"), ps.creer("4fro", "napo", "M").garnitures);
        assertEquals(List.of("jambon", "champignons"), ps.creer("regina", "napo", "M").garnitures);
        assertEquals(List.of("poivrons", "champignons", "oignons", "olives"), ps.creer("vege", "napo", "M").garnitures);
    }

    // ---------- Pizza du jour ----------

    @Test
    void pizzaDuJour() {
        Pizza p = ps.pizzaDuJour();
        assertEquals("Pizza du jour", p.nom);
        assertEquals("regina", p.type);
        assertEquals("L", p.taille);
        assertEquals("napo", p.style);
        assertTrue(p.extraFromage);
        assertEquals(12.00, p.prix, DELTA);
        assertEquals(List.of("jambon", "champignons", "artichauts"), p.garnitures);
    }

    @Test
    void pizzaDuJourRenvoieUneNouvelleInstanceAChaqueAppel() {
        assertNotSame(ps.pizzaDuJour(), ps.pizzaDuJour());
    }

    // ---------- Ticket de commande ----------

    private List<Pizza> commandeType() {
        List<Pizza> l = new ArrayList<>();
        l.add(ps.creer("marg", "napo", "M"));   // 8.00
        l.add(ps.creer("regina", "ny", "L"));   // 14.00
        return l;
    }

    @Test
    void ticketSansRemise() {
        String attendu =
                "=== Pizzeria Da Mario ===\n" +
                "Commande n°1\n" +
                "- Margherita (M, napo) : 8.00 EUR\n" +
                "- Regina (L, ny) : 14.00 EUR\n" +
                "Total HT : 22.00 EUR\n" +
                "TVA : 2.20 EUR\n" +
                "Total TTC : 24.20 EUR\n";
        assertEquals(attendu, ps.commande(commandeType(), false, false));
    }

    @Test
    void ticketClientFideleMoinsDe50Euros() {
        String attendu =
                "=== Pizzeria Da Mario ===\n" +
                "Commande n°1\n" +
                "- Margherita (M, napo) : 8.00 EUR\n" +
                "- Regina (L, ny) : 14.00 EUR\n" +
                "Remise fidélité : -10%\n" +
                "Total HT : 19.80 EUR\n" +
                "TVA : 1.98 EUR\n" +
                "Total TTC : 21.78 EUR\n";
        assertEquals(attendu, ps.commande(commandeType(), false, true));
    }

    @Test
    void ticketClientFidele50EurosOuPlus() {
        List<Pizza> l = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            l.add(ps.creer("4fro", "ny", "L")); // 4 x 15.30 = 61.20
        }
        String ticket = ps.commande(l, false, true);
        assertTrue(ticket.contains("Remise fidélité : -15%\n"));
        assertTrue(ticket.contains("Total HT : 52.02 EUR\n"));
        assertTrue(ticket.contains("TVA : 5.20 EUR\n"));
        assertTrue(ticket.contains("Total TTC : 57.22 EUR\n"));
    }

    @Test
    void ticketAEmporter() {
        String ticket = ps.commande(commandeType(), true, false);
        assertTrue(ticket.contains("Remise à emporter : -5%\n"));
        assertTrue(ticket.contains("Total HT : 20.90 EUR\n"));
        assertTrue(ticket.contains("TVA : 2.09 EUR\n"));
        assertTrue(ticket.contains("Total TTC : 22.99 EUR\n"));
    }

    @Test
    void ticketFideleEtAEmporterRemisesCumulees() {
        String ticket = ps.commande(commandeType(), true, true);
        assertTrue(ticket.indexOf("Remise fidélité") < ticket.indexOf("Remise à emporter"),
                "la remise fidélité est appliquée avant la remise à emporter");
        assertTrue(ticket.contains("Total HT : 18.81 EUR\n"));
        assertTrue(ticket.contains("TVA : 1.88 EUR\n"));
        assertTrue(ticket.contains("Total TTC : 20.69 EUR\n"));
    }

    @Test
    void lesPizzasNullSontIgnorees() {
        List<Pizza> l = Arrays.asList(ps.creer("marg", "napo", "M"), null, ps.creer("hawai", "ny", "M"));
        String ticket = ps.commande(l, false, false);
        assertTrue(ticket.contains("Total HT : 8.00 EUR\n"));
    }

    @Test
    void numeroDeCommandeIncremente() {
        assertTrue(ps.commande(commandeType(), false, false).contains("Commande n°1\n"));
        assertTrue(ps.commande(commandeType(), false, false).contains("Commande n°2\n"));
    }

    @Test
    void numeroDeCommandePartageEntreInstances() {
        new PizzeriaService().commande(commandeType(), false, false);
        String ticket = new PizzeriaService().commande(commandeType(), false, false);
        assertTrue(ticket.contains("Commande n°2\n"));
    }

    // ---------- Enregistrement des commandes ----------

    @Test
    void laCommandeEstEnregistree() {
        String ticket = ps.commande(commandeType(), true, false);
        Commande c = ps.get(1);
        assertNotNull(c);
        assertEquals(1, c.num);
        assertEquals(2, c.pizzas.size());
        assertTrue(c.emp);
        assertFalse(c.fid);
        assertEquals(20.90, c.ht, DELTA);
        assertEquals(2.09, c.tva, DELTA);
        assertEquals(22.99, c.ttc, DELTA);
        assertEquals(ticket, c.ticket);
    }

    @Test
    void lesPizzasNullNeSontPasEnregistrees() {
        ps.commande(Arrays.asList(ps.creer("marg", "napo", "M"), null), false, false);
        assertEquals(1, ps.get(1).pizzas.size());
    }

    @Test
    void commandeInconnueRetourneNull() {
        assertNull(ps.get(42));
    }

    @Test
    void historiqueDesCommandes() {
        ps.commande(commandeType(), false, false);
        ps.commande(commandeType(), true, true);
        assertEquals(2, ps.historique().size());
        assertEquals(2, ps.historique().get(1).num);
    }

    @Test
    void chiffreDAffaires() {
        ps.commande(commandeType(), false, false); // 24.20
        ps.commande(commandeType(), true, true);   // 20.69
        assertEquals(44.89, ps.ca(), DELTA);
    }

    @Test
    void chiffreDAffairesSansCommande() {
        assertEquals(0.0, ps.ca(), DELTA);
    }

    // ---------- Statistiques ----------

    @Test
    void nombreDePizzasVendues() {
        ps.commande(commandeType(), false, false);
        ps.commande(List.of(ps.creer("regina", "napo", "S")), false, false);
        assertEquals(2, ps.nbVendues("regina"));
        assertEquals(1, ps.nbVendues("marg"));
        assertEquals(0, ps.nbVendues("vege"));
    }

    @Test
    void pizzaCreeeMaisNonCommandeeNestPasComptee() {
        ps.creer("vege", "napo", "M");
        assertEquals(0, ps.nbVendues("vege"));
    }

    // ---------- Configuration ----------

    @Test
    void configurationDeLaTva() {
        ps.config("tva=0.2");
        String ticket = ps.commande(commandeType(), false, false);
        assertTrue(ticket.contains("TVA : 4.40 EUR\n"));
        assertTrue(ticket.contains("Total TTC : 26.40 EUR\n"));
    }

    @Test
    void configurationDuNom() {
        ps.config("nom=Bella Napoli");
        assertTrue(ps.commande(commandeType(), false, false).startsWith("=== Bella Napoli ===\n"));
    }

    @Test
    void configurationInvalideEstIgnoree() {
        ps.config("tva=abc");
        ps.config("n'importe quoi");
        assertTrue(ps.commande(commandeType(), false, false).contains("TVA : 2.20 EUR\n"));
    }
}
