package fr.tp.pizzeria.model;

/** Les styles proposés et les éléments de pâte et de sauce associés. */
public enum StylePizza {
    NAPOLITAINE("napo", "fine", "tomate San Marzano"),
    NEW_YORKAISE("ny", "épaisse", "tomate origan");

    private final String code;
    private final String pate;
    private final String sauce;

    StylePizza(String code, String pate, String sauce) {
        this.code = code;
        this.pate = pate;
        this.sauce = sauce;
    }

    public String getCode() {
        return code;
    }

    public String getPate() {
        return pate;
    }

    public String getSauce() {
        return sauce;
    }

    public String getFromage(TypePizza type) {
        if (this == NEW_YORKAISE && type != TypePizza.VEGETARIENNE) {
            return "mozzarella râpée";
        }
        return "mozzarella di bufala";
    }

    public static StylePizza depuisCode(String code) {
        for (StylePizza style : values()) {
            if (style.code.equals(code)) {
                return style;
            }
        }
        return null;
    }
}
