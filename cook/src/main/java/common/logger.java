package common;

public class logger {

    public static void info(String message) {
        System.out.println("[INFO] " + message);
    }

    public static void warn(String message) {
        System.out.println("[WARN] " + message);
    }

    public static void error(String message) {
        System.err.println("[ERROR] " + message);
    }

    public static void info(String string, String name, String format) {
        String message = String.format(format, string, name);
        System.out.println("[INFO] " + message);
    }
    

}
