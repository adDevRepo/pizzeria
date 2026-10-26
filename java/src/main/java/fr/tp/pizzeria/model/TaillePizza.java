package fr.tp.pizzeria.model;

/** Les tailles connues. Le coefficient reste dans la configuration pour être ajustable. */
public enum TaillePizza {
    PETITE("S"),
    MOYENNE("M"),
    GRANDE("L");

    private final String code;

    TaillePizza(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static TaillePizza depuisCode(String code) {
        for (TaillePizza taille : values()) {
            if (taille.code.equals(code)) {
                return taille;
            }
        }
        return null;
    }
}
