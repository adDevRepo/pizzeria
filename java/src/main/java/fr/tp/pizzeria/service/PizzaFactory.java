package fr.tp.pizzeria.service;

import fr.tp.pizzeria.model.Pizza;
import fr.tp.pizzeria.model.StylePizza;
import fr.tp.pizzeria.model.TaillePizza;
import fr.tp.pizzeria.model.TypePizza;
import fr.tp.pizzeria.repository.ConfigRepository;

import java.util.ArrayList;

/**
 * Fabrique les pizzas
 *
 */
public class PizzaFactory {
    public Pizza creer(String codeType, String codeStyle, String codeTaille,
                       boolean extraFromage, boolean sansGluten) {
        TypePizza type = TypePizza.depuisCode(codeType);
        StylePizza style = StylePizza.depuisCode(codeStyle);

        // Un code non reconnu ne correspond à aucune pizza du menu.
        if (type == null || style == null) {
            return null;
        }

        // Les tailles connues sont normalisées par l'enum.
        TaillePizza taille = TaillePizza.depuisCode(codeTaille);
        // Une taille inconnue garde le code reçu et le coefficient neutre.
        String codeTailleConserve = taille == null ? codeTaille : taille.getCode();


        Pizza pizza = new Pizza.Builder()
                .nom(type.getNom())
                .type(type.getCode())
                .taille(codeTailleConserve)
                .style(style.getCode())
                .pate(style.getPate())
                .sauce(style.getSauce())
                .fromage(style.getFromage(type))
                .garnitures(new ArrayList<>(type.getGarnitures()))
                .extraFromage(extraFromage)
                .sansGluten(sansGluten)
                .prix(0)
                .build();

        pizza.prix = calculerPrix(pizza);
        return pizza;
    }

    // Calcule du prix
    public double calculerPrix(Pizza pizza) {
        TypePizza type = TypePizza.depuisCode(pizza.type);
        StylePizza style = StylePizza.depuisCode(pizza.style);
        TaillePizza taille = TaillePizza.depuisCode(pizza.taille);

        String codeType = type == null ? pizza.type : type.getCode();
        String codeTaille = taille == null ? pizza.taille : taille.getCode();

        double prix = Double.parseDouble(
                ConfigRepository.params.getOrDefault("prix." + codeType, "0"));
        double coefficientTaille = Double.parseDouble(
                ConfigRepository.params.getOrDefault("taille." + codeTaille, "1.0"));
        prix *= coefficientTaille;

        if (style == StylePizza.NEW_YORKAISE) {
            prix += Double.parseDouble(ConfigRepository.params.get("supplement.style.ny"));
        }
        if (pizza.extraFromage) {
            prix += Double.parseDouble(ConfigRepository.params.get("supplement.fromage"));
        }
        if (pizza.sansGluten) {
            prix += Double.parseDouble(ConfigRepository.params.get("supplement.sansGluten"));
        }

        return Math.round(prix * 100) / 100.0;
    }
}
