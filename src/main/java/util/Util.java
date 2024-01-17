package util;

import java.util.HashMap;
import java.util.Map;

public class Util {

    private static final String REQUEST_LINE_DELIMITER = " ";
    private static final String REQUEST_HEADER_DELIMITER = ": ";
    private static final String URL_DELIMITER = "\\?";
    private static final String PARAMETERS_DELIMITER = "&";
    private static final String PARAMETER_DELIMITER = "=";

    public static String[] splitRequestLine(String input) {
        return input.split(REQUEST_LINE_DELIMITER);
    }

    public static String[] splitRequestHeader(String input) {
        return input.split(REQUEST_HEADER_DELIMITER);
    }

    public static String[] splitUrlToPathAndParameters(String url) {
        return url.split(URL_DELIMITER);
    }

    public static Map<String, String> splitParamtersToKeyAndValue(String parameters) throws IllegalArgumentException{
        Map<String, String> queryParameters = new HashMap<>();
        for (String parameter: parameters.split(PARAMETERS_DELIMITER)) {
            String[] token = parameter.split(PARAMETER_DELIMITER);
            queryParameters.put(token[0], token[1]);
        }
        return queryParameters;
    }
}
