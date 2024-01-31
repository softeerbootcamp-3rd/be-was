package util;

public class UriParser {
    public static String getFileType(String uri) {
        int index = uri.lastIndexOf(".");
        return uri.substring(index + 1);
    }
}
