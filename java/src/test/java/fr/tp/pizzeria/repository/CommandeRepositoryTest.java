package fr.tp.pizzeria.repository;

import fr.tp.pizzeria.model.Commande;
import fr.tp.pizzeria.model.Pizza;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de caractérisation de la couche repository.
 */
class CommandeRepositoryTest {

    private static final double DELTA = 0.001;

    private CommandeRepository repo;

    @BeforeEach
    void setUp() {
        CommandeRepository.data.clear();
        CommandeRepository.cpt = 0;
        repo = new CommandeRepository();
    }

    private Pizza pizza(String type) {
        return new Pizza("test", type, "M", "napo", "fine", "tomate", "mozzarella",
                new ArrayList<>(), false, false, 10.0);
    }

    private Commande commande(int num, double ttc, String... types) {
        Commande c = new Commande();
        c.num = num;
        c.ttc = ttc;
        c.pizzas = new ArrayList<>();
        for (String t : types) {
            c.pizzas.add(pizza(t));
        }
        return c;
    }

    @Test
    void lesNumerosSeSuivent() {
        assertEquals(1, repo.prochainNumero());
        assertEquals(2, repo.prochainNumero());
        assertEquals(3, new CommandeRepository().prochainNumero());
    }

    @Test
    void saveEtFindByNum() {
        Commande c = commande(7, 15.0, "marg");
        repo.save(c);
        assertSame(c, repo.findByNum(7));
        assertNull(repo.findByNum(8));
    }

    @Test
    void getAllRetourneLesCommandesDansLOrdreDInsertion() {
        repo.save(commande(1, 10.0, "marg"));
        repo.save(commande(2, 20.0, "vege"));
        List<Commande> toutes = repo.getAll();
        assertEquals(2, toutes.size());
        assertEquals(1, toutes.get(0).num);
        assertEquals(2, toutes.get(1).num);
    }

    @Test
    void lesDonneesSontPartageesEntreInstances() {
        repo.save(commande(1, 10.0, "marg"));
        assertEquals(1, new CommandeRepository().getAll().size());
    }

    @Test
    void compterParType() {
        repo.save(commande(1, 10.0, "marg", "regina", "marg"));
        repo.save(commande(2, 10.0, "regina"));
        assertEquals(2, repo.compter("marg"));
        assertEquals(2, repo.compter("regina"));
        assertEquals(0, repo.compter("4fro"));
    }

    @Test
    void chiffreDAffaires() {
        repo.save(commande(1, 10.10, "marg"));
        repo.save(commande(2, 20.20, "marg"));
        assertEquals(30.30, repo.ca(), DELTA);
    }
}
