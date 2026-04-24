package burger.model;

import java.util.ArrayList;
import java.util.List;

public class Burger implements MenuItem {
    private final String name;
    private final int price;
    private final List<Ingredient> ingredients;

    public Burger(String name, int price, List<Ingredient> ingredients) {
        this.name = name;
        this.price = price;
        this.ingredients = new ArrayList<>(ingredients);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public List<Ingredient> getIngredients() {
        return new ArrayList<>(ingredients);
    }
}