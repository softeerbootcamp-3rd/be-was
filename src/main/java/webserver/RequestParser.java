package webserver;

import dto.HttpRequest;

import java.io.IOException;

import static common.util.Util.split;


public class RequestParser {

    public static final String REQUEST_LINE_DELIMITER = " ";
    public static final String REQUEST_HEADER_DELIMITER = ": ";
    public static final String URL_DELIMITER = "\\?";
    public static final String PARAMETERS_DELIMITER = "&";
    public static final String PARAMETER_DELIMITER = "=";

    public static HttpRequest parseRequestLine(String line) throws IOException {
        String[] methodAndURL = split(line, REQUEST_LINE_DELIMITER);
        if (isQueryStringExist(methodAndURL[1])) {
            String[] pathAndQueryString = split(methodAndURL[1], URL_DELIMITER);
            return new HttpRequest(methodAndURL[0], pathAndQueryString[0], pathAndQueryString[1]);
        }
        return new HttpRequest(methodAndURL[0], methodAndURL[1]);
    }

    public static String[] parseRequestHeader(String line) {
        return split(line, REQUEST_HEADER_DELIMITER);
    }

    private static boolean isQueryStringExist(String url) {
        return url.contains("?");
    }
}
