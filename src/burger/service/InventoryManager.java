package burger.service;

import burger.model.Ingredient;
import burger.model.Ingredients;
import burger.model.MenuItem;
import burger.model.Order;
import burger.model.OrderItem;
import burger.util.ConsolePrinter;

import java.util.LinkedHashMap;
import java.util.Map;

public class InventoryManager {
    private final Map<String, Integer> stockMap = new LinkedHashMap<>();

    public InventoryManager() {
        stockMap.put(Ingredients.BEEF_PATTY.getName(), 20);
        stockMap.put(Ingredients.CHICKEN_PATTY.getName(), 20);
        stockMap.put(Ingredients.CHEESE.getName(), 30);
        stockMap.put(Ingredients.LETTUCE.getName(), 30);
        stockMap.put(Ingredients.TOMATO.getName(), 25);
        stockMap.put(Ingredients.PICKLE.getName(), 30);
        stockMap.put(Ingredients.RAW_ONION.getName(), 25);
        stockMap.put(Ingredients.GRILLED_ONION.getName(), 25);
        stockMap.put(Ingredients.BACON.getName(), 15);
        stockMap.put(Ingredients.KETCHUP.getName(), 30);
        stockMap.put(Ingredients.MAYO.getName(), 30);
        stockMap.put(Ingredients.MUSTARD.getName(), 30);
        stockMap.put(Ingredients.SPICY.getName(), 30);
    }

    public boolean hasEnoughIngredients(Order order) {
        Map<String, Integer> required = calculateRequiredIngredients(order);

        for (String ingredientName : required.keySet()) {
            int need = required.get(ingredientName);
            int stock = stockMap.getOrDefault(ingredientName, 0);

            if (stock < need) {
                ConsolePrinter.printLine("부족한 재료: " + ingredientName
                        + " / 필요: " + need
                        + "개 / 현재: " + stock + "개");
                return false;
            }
        }

        return true;
    }

    public void consumeIngredients(Order order) {
        Map<String, Integer> required = calculateRequiredIngredients(order);

        for (String ingredientName : required.keySet()) {
            int need = required.get(ingredientName);
            stockMap.put(ingredientName, stockMap.get(ingredientName) - need);
        }
    }

    private Map<String, Integer> calculateRequiredIngredients(Order order) {
        Map<String, Integer> required = new LinkedHashMap<>();

        for (OrderItem orderItem : order.getOrderItems()) {
            MenuItem menuItem = orderItem.getItem();
            int quantity = orderItem.getQuantity();

            for (Ingredient ingredient : menuItem.getIngredients()) {
                required.merge(ingredient.getName(), quantity, Integer::sum);
            }
        }

        return required;
    }

    public void printInventory() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n[현재 재고]\n");

        stockMap.forEach((name, count) -> {
            sb.append(name)
                    .append(": ")
                    .append(count)
                    .append("개\n");
        });

        ConsolePrinter.printBlock(sb.toString());
    }
}