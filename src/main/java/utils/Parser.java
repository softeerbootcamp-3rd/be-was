package utils;

public class Parser {

    public static String[] parsing(String request, String delimiter, int limit) {
        if (limit < 0) {
            return request.split(delimiter);
        }
        return request.split(delimiter, limit);
    }

    public static String extractQuery(String request) {
        String[] tokens = request.split("\\?", 2);
        return tokens[1];
    }
}
