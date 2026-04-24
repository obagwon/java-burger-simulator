package burger.service;

import burger.model.MenuItem;
import burger.model.Order;
import burger.model.OrderItem;
import burger.policy.PricingPolicy;
import burger.util.ConsolePrinter;

import java.util.ArrayList;
import java.util.List;

public class OrderManager {
    private final List<Order> orders = new ArrayList<>();
    private int nextOrderId = 1001;

    public Order createOrder(String customerName, MenuItem item, int quantity, PricingPolicy pricingPolicy) {
        List<OrderItem> orderItems = List.of(new OrderItem(item, quantity));
        return new Order(nextOrderId++, customerName, orderItems, pricingPolicy);
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public List<Order> getOrders() {
        return new ArrayList<>(orders);
    }

    public void printOrders() {
        ConsolePrinter.printLine("\n[주문 내역]");

        if (orders.isEmpty()) {
            ConsolePrinter.printLine("아직 주문 내역이 없습니다.");
            return;
        }

        for (Order order : orders) {
            ConsolePrinter.printLine("--------------------------------");
            order.printOrderInfo();
        }
    }
}