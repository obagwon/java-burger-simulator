package burger.factory;

import burger.model.Burger;
import burger.model.Ingredients;
import burger.model.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MenuFactory {
    public static List<MenuItem> getDefaultMenus() {
        List<MenuItem> menus = new ArrayList<>();

        menus.add(createBurger(1));
        menus.add(createBurger(2));
        menus.add(createBurger(3));
        menus.add(createBurger(4));
        menus.add(createBurger(5));

        return menus;
    }

    public static Burger createBurger(int menuNumber) {
        return switch (menuNumber) {
            case 1 -> new Burger(
                    "클래식버거",
                    4500,
                    List.of(
                            Ingredients.BEEF_PATTY,
                            Ingredients.LETTUCE,
                            Ingredients.PICKLE,
                            Ingredients.KETCHUP
                    )
            );

            case 2 -> new Burger(
                    "치즈버거",
                    5000,
                    List.of(
                            Ingredients.BEEF_PATTY,
                            Ingredients.CHEESE,
                            Ingredients.LETTUCE,
                            Ingredients.PICKLE,
                            Ingredients.KETCHUP
                    )
            );

            case 3 -> new Burger(
                    "더블불고기버거",
                    7000,
                    List.of(
                            Ingredients.BEEF_PATTY,
                            Ingredients.BEEF_PATTY,
                            Ingredients.CHEESE,
                            Ingredients.GRILLED_ONION,
                            Ingredients.MUSTARD
                    )
            );

            case 4 -> new Burger(
                    "치킨버거",
                    5500,
                    List.of(
                            Ingredients.CHICKEN_PATTY,
                            Ingredients.LETTUCE,
                            Ingredients.TOMATO,
                            Ingredients.MAYO
                    )
            );

            case 5 -> new Burger(
                    "베이컨버거",
                    6500,
                    List.of(
                            Ingredients.BEEF_PATTY,
                            Ingredients.BACON,
                            Ingredients.CHEESE,
                            Ingredients.LETTUCE,
                            Ingredients.MUSTARD
                    )
            );

            default -> throw new IllegalArgumentException("존재하지 않는 메뉴 번호입니다.");
        };
    }

    private MenuFactory() {
    }
}