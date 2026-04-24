package burger.model;

public class Ingredients {
    public static final Ingredient BEEF_PATTY = new Ingredient("소고기 패티", 1800);
    public static final Ingredient CHICKEN_PATTY = new Ingredient("치킨 패티", 1600);
    public static final Ingredient CHEESE = new Ingredient("치즈", 500);
    public static final Ingredient LETTUCE = new Ingredient("양상추", 300);
    public static final Ingredient TOMATO = new Ingredient("토마토", 400);
    public static final Ingredient PICKLE = new Ingredient("피클", 200);
    public static final Ingredient RAW_ONION = new Ingredient("생양파", 200);
    public static final Ingredient GRILLED_ONION = new Ingredient("구운양파", 300);
    public static final Ingredient BACON = new Ingredient("베이컨", 1000);

    public static final Ingredient KETCHUP = new Ingredient("케첩", 200);
    public static final Ingredient MAYO = new Ingredient("마요", 200);
    public static final Ingredient MUSTARD = new Ingredient("머스타드", 200);
    public static final Ingredient SPICY = new Ingredient("스파이시 소스", 300);

    private Ingredients() {
    }
}