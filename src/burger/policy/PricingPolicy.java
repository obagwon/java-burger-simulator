package burger.policy;

import burger.model.OrderItem;

public interface PricingPolicy {
    int calculate(OrderItem orderItem);
}