package burger.model;

import java.util.ArrayList;
import java.util.List;

public class CustomBurger extends Burger {
    private CustomBurger(String name, int price, List<Ingredient> ingredients) {
        super(name, price, ingredients);
    }

    public static class Builder {
        private final List<Ingredient> ingredients = new ArrayList<>();
        private final int basePrice = 2000;

        public Builder patty(String pattyType, int count) {
            Ingredient patty = pattyType.equals("BEEF")
                    ? Ingredients.BEEF_PATTY
                    : Ingredients.CHICKEN_PATTY;

            for (int i = 0; i < count; i++) {
                ingredients.add(patty);
            }

            return this;
        }

        public Builder cheese(int count) {
            for (int i = 0; i < count; i++) {
                ingredients.add(Ingredients.CHEESE);
            }

            return this;
        }

        public Builder lettuce() {
            ingredients.add(Ingredients.LETTUCE);
            return this;
        }

        public Builder tomato() {
            ingredients.add(Ingredients.TOMATO);
            return this;
        }

        public Builder pickle() {
            ingredients.add(Ingredients.PICKLE);
            return this;
        }

        public Builder rawOnion() {
            ingredients.add(Ingredients.RAW_ONION);
            return this;
        }

        public Builder grilledOnion() {
            ingredients.add(Ingredients.GRILLED_ONION);
            return this;
        }

        public Builder bacon() {
            ingredients.add(Ingredients.BACON);
            return this;
        }

        public Builder sauce(Ingredient sauce) {
            ingredients.add(sauce);
            return this;
        }

        public CustomBurger build() {
            int ingredientPrice = ingredients.stream()
                    .mapToInt(Ingredient::getPrice)
                    .sum();

            int totalPrice = basePrice + ingredientPrice;

            return new CustomBurger("커스텀 버거", totalPrice, ingredients);
        }
    }
}