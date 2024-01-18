package util;

public class Util {

    private static final String REQUEST_LINE_DELIMITER = " ";
    private static final String REQUEST_HEADER_DELIMITER = ": ";
    private static final String URL_DELIMITER = "\\?";
    private static final String PARAMETERS_DELIMITER = "&";
    private static final String PARAMETER_DELIMITER = "=";

    public static String[] splitRequestLineToMethodAndURL(String input) {
        return input.split(REQUEST_LINE_DELIMITER);
    }

    public static String[] splitRequestHeader(String input) {
        return input.split(REQUEST_HEADER_DELIMITER);
    }

    public static String[] splitUrlToPathAndQueryString(String url) {
        return url.split(URL_DELIMITER);
    }

    public static String[] splitParamters(String parameters) {
        return parameters.split(PARAMETERS_DELIMITER);
    }

    public static String[] splitParameter(String parameter) {
        return parameter.split(PARAMETER_DELIMITER);
    }
}
