package util;

public class HttpRequestUtils {
    public static String getUrl(String firstLine) {
        String[] tokens = firstLine.split(" ");
        String url = tokens[1];
        if (url.equals("/"))
            url = "/index.html";
        return url;
    }
}
