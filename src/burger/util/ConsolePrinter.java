package burger.util;

public class ConsolePrinter {
    private static final Object lock = new Object();

    public static void printLine(String message) {
        synchronized (lock) {
            System.out.println(message);
        }
    }

    public static void printBlock(String message) {
        synchronized (lock) {
            System.out.print(message);
        }
    }

    public static void printPrompt(String message) {
        synchronized (lock) {
            System.out.print(message);
        }
    }

    private ConsolePrinter() {
    }
}