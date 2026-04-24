package burger;

import burger.factory.MenuFactory;
import burger.model.CustomBurger;
import burger.model.Ingredients;
import burger.model.MenuItem;
import burger.model.Order;
import burger.policy.BasicPricingPolicy;
import burger.policy.PricingPolicy;
import burger.service.CookingService;
import burger.service.InventoryManager;
import burger.service.OrderManager;
import burger.service.StatisticsService;
import burger.util.ConsolePrinter;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Store {
    private final Scanner scanner = new Scanner(System.in);

    private final InventoryManager inventoryManager = new InventoryManager();
    private final OrderManager orderManager = new OrderManager();
    private final CookingService cookingService = new CookingService();
    private final PricingPolicy pricingPolicy = new BasicPricingPolicy();
    private final StatisticsService statisticsService = new StatisticsService(orderManager);

    private String customerName = "손님";

    public void start() {
        ConsolePrinter.printBlock("""
                ================================
                 Burger Builder
                 콘솔 햄버거 주문 시뮬레이터
                ================================
                """);

        ConsolePrinter.printPrompt("고객 이름을 입력하세요: ");
        String inputName = scanner.nextLine().trim();

        if (!inputName.isEmpty()) {
            customerName = inputName;
        }

        while (true) {
            printNotifications();
            printMainMenu();

            int choice = readInt("메뉴를 선택하세요: ");

            switch (choice) {
                case 1 -> showMenu();
                case 2 -> orderBasicBurger();
                case 3 -> orderCustomBurger();
                case 4 -> orderManager.printOrders();
                case 5 -> inventoryManager.printInventory();
                case 6 -> statisticsService.printStatistics();
                case 0 -> {
                    ConsolePrinter.printLine("프로그램을 종료합니다.");
                    cookingService.shutdown();
                    return;
                }
                default -> ConsolePrinter.printLine("잘못된 입력입니다.");
            }
        }
    }

    private void printNotifications() {
        List<String> notifications = cookingService.getAndClearNotifications();

        if (notifications.isEmpty()) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\n[조리 알림]\n");

        for (String notification : notifications) {
            sb.append("- ").append(notification).append("\n");
        }

        ConsolePrinter.printBlock(sb.toString());
    }

    private void printMainMenu() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n");
        sb.append("================================\n");
        sb.append("1. 전체 메뉴 보기\n");
        sb.append("2. 기본 버거 주문\n");
        sb.append("3. 커스텀 버거 주문\n");
        sb.append("4. 주문 내역 보기\n");
        sb.append("5. 재고 확인\n");
        sb.append("6. 매출 / 인기 메뉴 통계\n");
        sb.append("0. 종료\n");
        sb.append("================================\n");

        ConsolePrinter.printBlock(sb.toString());
    }

    private void showMenu() {
        List<MenuItem> menus = MenuFactory.getDefaultMenus();

        StringBuilder sb = new StringBuilder();
        sb.append("\n[기본 메뉴 목록]\n");

        for (int i = 0; i < menus.size(); i++) {
            MenuItem menu = menus.get(i);
            sb.append(i + 1)
                    .append(". ")
                    .append(menu.getName())
                    .append(" - ")
                    .append(menu.getPrice())
                    .append("원\n");
            sb.append("   재료: ")
                    .append(menu.getDescription())
                    .append("\n");
        }

        sb.append("\n[가격 낮은 순 정렬]\n");

        menus.stream()
                .sorted(Comparator.comparingInt(MenuItem::getPrice))
                .forEach(menu -> sb.append("- ")
                        .append(menu.getName())
                        .append(": ")
                        .append(menu.getPrice())
                        .append("원\n"));

        ConsolePrinter.printBlock(sb.toString());
    }

    private void orderBasicBurger() {
        List<MenuItem> menus = MenuFactory.getDefaultMenus();

        StringBuilder sb = new StringBuilder();
        sb.append("\n[기본 버거 주문]\n");

        for (int i = 0; i < menus.size(); i++) {
            sb.append(i + 1)
                    .append(". ")
                    .append(menus.get(i).getName())
                    .append(" - ")
                    .append(menus.get(i).getPrice())
                    .append("원\n");
        }

        ConsolePrinter.printBlock(sb.toString());

        int menuNumber = readInt("주문할 메뉴 번호: ");

        if (menuNumber < 1 || menuNumber > menus.size()) {
            ConsolePrinter.printLine("존재하지 않는 메뉴입니다.");
            return;
        }

        int quantity = readInt("수량: ");

        if (quantity <= 0) {
            ConsolePrinter.printLine("수량은 1개 이상이어야 합니다.");
            return;
        }

        MenuItem selectedBurger = MenuFactory.createBurger(menuNumber);
        Order order = orderManager.createOrder(customerName, selectedBurger, quantity, pricingPolicy);

        if (!inventoryManager.hasEnoughIngredients(order)) {
            ConsolePrinter.printLine("재고가 부족하여 주문할 수 없습니다.");
            return;
        }

        String confirmMessage = "\n[주문 확인]\n"
                + "메뉴: " + selectedBurger.getName() + "\n"
                + "수량: " + quantity + "\n"
                + "총액: " + order.getTotalPrice() + "원\n";

        ConsolePrinter.printBlock(confirmMessage);

        if (!readYesNo("주문하시겠습니까? (Y/N): ")) {
            ConsolePrinter.printLine("주문이 취소되었습니다.");
            return;
        }

        inventoryManager.consumeIngredients(order);
        orderManager.addOrder(order);
        cookingService.addOrder(order);

        ConsolePrinter.printLine("주문이 접수되었습니다.");
        ConsolePrinter.printLine("주문번호: " + order.getOrderId());
    }

    private void orderCustomBurger() {
        ConsolePrinter.printLine("\n[커스텀 버거 만들기]");

        CustomBurger.Builder builder = new CustomBurger.Builder();

        ConsolePrinter.printBlock("""
                1단계. 패티 종류를 선택하세요.
                1. 소고기 패티
                2. 치킨 패티
                """);

        int pattyType = readInt("선택: ");

        String selectedPatty;

        if (pattyType == 1) {
            selectedPatty = "BEEF";
        } else if (pattyType == 2) {
            selectedPatty = "CHICKEN";
        } else {
            ConsolePrinter.printLine("잘못된 패티 선택입니다.");
            return;
        }

        int pattyCount = readInt("2단계. 패티 개수: ");

        if (pattyCount <= 0) {
            ConsolePrinter.printLine("패티는 1개 이상 선택해야 합니다.");
            return;
        }

        builder.patty(selectedPatty, pattyCount);

        int cheeseCount = readInt("3단계. 치즈 개수: ");

        if (cheeseCount < 0) {
            ConsolePrinter.printLine("치즈 개수는 0개 이상이어야 합니다.");
            return;
        }

        builder.cheese(cheeseCount);

        if (readYesNo("4단계. 양상추를 추가하시겠습니까? (Y/N): ")) {
            builder.lettuce();
        }

        if (readYesNo("5단계. 토마토를 추가하시겠습니까? (Y/N): ")) {
            builder.tomato();
        }

        if (readYesNo("6단계. 피클을 추가하시겠습니까? (Y/N): ")) {
            builder.pickle();
        }

        ConsolePrinter.printBlock("""
                7단계. 양파 종류를 선택하세요.
                0. 없음
                1. 생양파
                2. 구운양파
                """);

        int onionChoice = readInt("선택: ");

        if (onionChoice == 1) {
            builder.rawOnion();
        } else if (onionChoice == 2) {
            builder.grilledOnion();
        } else if (onionChoice != 0) {
            ConsolePrinter.printLine("잘못된 양파 선택입니다.");
            return;
        }

        if (readYesNo("8단계. 베이컨을 추가하시겠습니까? (Y/N): ")) {
            builder.bacon();
        }

        ConsolePrinter.printBlock("""
                9단계. 소스를 선택하세요. 하나 이상 선택해야 합니다.
                1. 케첩
                2. 마요
                3. 머스타드
                4. 스파이시 소스
                """);

        ConsolePrinter.printPrompt("선택 예시: 1 4 → ");
        String sauceInput = scanner.nextLine().trim();
        Set<Integer> sauceNumbers = parseNumbers(sauceInput);

        if (sauceNumbers.isEmpty()) {
            ConsolePrinter.printLine("소스는 하나 이상 선택해야 합니다.");
            return;
        }

        for (int sauceNumber : sauceNumbers) {
            switch (sauceNumber) {
                case 1 -> builder.sauce(Ingredients.KETCHUP);
                case 2 -> builder.sauce(Ingredients.MAYO);
                case 3 -> builder.sauce(Ingredients.MUSTARD);
                case 4 -> builder.sauce(Ingredients.SPICY);
                default -> {
                    ConsolePrinter.printLine("잘못된 소스 번호가 포함되어 있습니다.");
                    return;
                }
            }
        }

        CustomBurger customBurger = builder.build();

        String resultMessage = "\n[커스텀 버거 구성 결과]\n"
                + "재료: " + customBurger.getDescription() + "\n"
                + "예상 가격: " + customBurger.getPrice() + "원\n";

        ConsolePrinter.printBlock(resultMessage);

        if (!readYesNo("이 구성으로 주문하시겠습니까? (Y/N): ")) {
            ConsolePrinter.printLine("주문이 취소되었습니다.");
            return;
        }

        Order order = orderManager.createOrder(customerName, customBurger, 1, pricingPolicy);

        if (!inventoryManager.hasEnoughIngredients(order)) {
            ConsolePrinter.printLine("재고가 부족하여 주문할 수 없습니다.");
            return;
        }

        inventoryManager.consumeIngredients(order);
        orderManager.addOrder(order);
        cookingService.addOrder(order);

        ConsolePrinter.printLine("주문이 접수되었습니다.");
        ConsolePrinter.printLine("주문번호: " + order.getOrderId());
    }

    private int readInt(String message) {
        while (true) {
            ConsolePrinter.printPrompt(message);
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                ConsolePrinter.printLine("숫자를 입력해주세요.");
            }
        }
    }

    private boolean readYesNo(String message) {
        while (true) {
            ConsolePrinter.printPrompt(message);
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.equals("Y")) {
                return true;
            }

            if (input.equals("N")) {
                return false;
            }

            ConsolePrinter.printLine("Y 또는 N으로 입력해주세요.");
        }
    }

    private Set<Integer> parseNumbers(String input) {
        Set<Integer> numbers = new LinkedHashSet<>();

        if (input.isBlank()) {
            return numbers;
        }

        String[] tokens = input.split("\\s+");

        for (String token : tokens) {
            try {
                numbers.add(Integer.parseInt(token));
            } catch (NumberFormatException e) {
                return new LinkedHashSet<>();
            }
        }

        return numbers;
    }
}