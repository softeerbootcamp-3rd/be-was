package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {

    private static final String REQUEST_LINE_DELIMITER = " ";
    private static final String REQUEST_HEADER_DELIMITER = ": ";
    private static final String URL_DELIMITER = "\\?";
    public static final String PARAMETERS_DELIMITER = "&";
    public static final String PARAMETER_DELIMITER = "=";

    public static String parseMethod(String line) {
        String[] tokens = line.split(REQUEST_LINE_DELIMITER);
        return tokens[0];
    }

    public static String[] parseUrl(String requestLine) {
        String[] tokens = requestLine.split(REQUEST_LINE_DELIMITER);
        String url = tokens[1];
        if (isQueryStringExist(url)) {
            tokens = url.split(URL_DELIMITER);
            return new String[]{tokens[0], tokens[1]};
        }
        return new String[]{url, null};
    }

    public static Map<String, String> parseHeaders(BufferedReader br, String line) throws IOException {
        String[] tokens;
        Map<String, String> headers = new HashMap<>();
        while (!line.equals("")) {
            line = br.readLine();
            if (line.contains(":")) {
                tokens = line.split(REQUEST_HEADER_DELIMITER);
                headers.put(tokens[0], tokens[1]);
            }
        }
        return headers;
    }

    private static boolean isQueryStringExist(String url) {
        return url.contains(URL_DELIMITER);
    }
}
