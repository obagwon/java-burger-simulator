package burger.service;

import burger.model.Order;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CookingService {
    private final BlockingQueue<Order> cookingQueue = new LinkedBlockingQueue<>();
    private final Queue<String> notificationQueue = new LinkedList<>();

    private final Thread cookingThread;
    private volatile boolean running = true;

    public CookingService() {
        cookingThread = new Thread(() -> {
            while (running) {
                try {
                    Order order = cookingQueue.take();
                    cook(order);
                } catch (InterruptedException e) {
                    if (!running) {
                        break;
                    }
                }
            }
        });

        cookingThread.setDaemon(true);
        cookingThread.start();
    }

    public void addOrder(Order order) {
        cookingQueue.offer(order);
    }

    private void cook(Order order) {
        try {
            addNotification("주문 #" + order.getOrderId()
                    + " " + order.getSummary()
                    + " 조리를 시작했습니다.");

            Thread.sleep(3000);

            addNotification("주문 #" + order.getOrderId()
                    + " " + order.getSummary()
                    + " 조리가 완료되었습니다.");

        } catch (InterruptedException e) {
            addNotification("주문 #" + order.getOrderId() + " 조리가 중단되었습니다.");
        }
    }

    private void addNotification(String message) {
        synchronized (notificationQueue) {
            notificationQueue.offer(message);
        }
    }

    public List<String> getAndClearNotifications() {
        List<String> notifications = new ArrayList<>();

        synchronized (notificationQueue) {
            while (!notificationQueue.isEmpty()) {
                notifications.add(notificationQueue.poll());
            }
        }

        return notifications;
    }

    public void shutdown() {
        running = false;
        cookingThread.interrupt();
    }
}