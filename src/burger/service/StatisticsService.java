package burger.service;

import burger.model.Order;
import burger.util.ConsolePrinter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsService {
    private final OrderManager orderManager;

    public StatisticsService(OrderManager orderManager) {
        this.orderManager = orderManager;
    }

    public void printStatistics() {
        List<Order> orders = orderManager.getOrders();

        StringBuilder sb = new StringBuilder();
        sb.append("\n[매출 / 인기 메뉴 통계]\n");

        if (orders.isEmpty()) {
            sb.append("아직 주문 내역이 없습니다.\n");
            ConsolePrinter.printBlock(sb.toString());
            return;
        }

        int totalSales = orders.stream()
                .mapToInt(Order::getTotalPrice)
                .sum();

        long totalOrderCount = orders.size();

        Map<String, Integer> menuCountMap = new HashMap<>();

        orders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .forEach(orderItem -> {
                    String menuName = orderItem.getItem().getName();
                    int quantity = orderItem.getQuantity();
                    menuCountMap.merge(menuName, quantity, Integer::sum);
                });

        sb.append("총 주문 수: ").append(totalOrderCount).append("건\n");
        sb.append("총 매출: ").append(totalSales).append("원\n");

        sb.append("\n[인기 메뉴 순위]\n");

        menuCountMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> {
                    sb.append(entry.getKey())
                            .append(" - ")
                            .append(entry.getValue())
                            .append("개\n");
                });

        ConsolePrinter.printBlock(sb.toString());
    }
}