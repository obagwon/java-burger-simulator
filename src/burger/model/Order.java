package burger.model;

import burger.policy.PricingPolicy;
import burger.util.ConsolePrinter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Order {
    private final int orderId;
    private final String customerName;
    private final List<OrderItem> orderItems;
    private final int totalPrice;
    private final LocalDateTime createdAt;

    public Order(int orderId, String customerName, List<OrderItem> orderItems, PricingPolicy pricingPolicy) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.orderItems = new ArrayList<>(orderItems);
        this.totalPrice = orderItems.stream()
                .mapToInt(item -> item.getSubTotal(pricingPolicy))
                .sum();
        this.createdAt = LocalDateTime.now();
    }

    public int getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public List<OrderItem> getOrderItems() {
        return new ArrayList<>(orderItems);
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getSummary() {
        return orderItems.stream()
                .map(item -> item.getItem().getName() + " x " + item.getQuantity())
                .collect(Collectors.joining(", "));
    }

    public void printOrderInfo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        StringBuilder sb = new StringBuilder();
        sb.append("주문번호: ").append(orderId).append("\n");
        sb.append("고객명: ").append(customerName).append("\n");
        sb.append("주문내용: ").append(getSummary()).append("\n");
        sb.append("총액: ").append(totalPrice).append("원\n");
        sb.append("주문시간: ").append(createdAt.format(formatter)).append("\n");

        ConsolePrinter.printBlock(sb.toString());
    }
}