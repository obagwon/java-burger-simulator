package burger.policy;

import burger.model.OrderItem;

public class BasicPricingPolicy implements PricingPolicy {
    @Override
    public int calculate(OrderItem orderItem) {
        return orderItem.getItem().getPrice() * orderItem.getQuantity();
    }
}