package util;

public class Util {

    private static final String REQUEST_LINE_DELIMITER = " ";
    private static final String REQUEST_HEADER_DELIMITER = ": ";

    public static String[] splitRequestLine(String input) {
        return input.split(REQUEST_LINE_DELIMITER);
    }

    public static String[] splitRequestHeader(String input) {
        return input.split(REQUEST_HEADER_DELIMITER);
    }
}
