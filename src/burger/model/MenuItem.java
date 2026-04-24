package burger.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface MenuItem {
    String getName();

    int getPrice();

    List<Ingredient> getIngredients();

    default String getDescription() {
        Map<String, Long> grouped = getIngredients().stream()
                .collect(Collectors.groupingBy(
                        Ingredient::getName,
                        LinkedHashMap::new,
                        Collectors.counting()
                ));

        return grouped.entrySet().stream()
                .map(entry -> {
                    if (entry.getValue() == 1) {
                        return entry.getKey();
                    }

                    return entry.getKey() + " x " + entry.getValue();
                })
                .collect(Collectors.joining(", "));
    }
}