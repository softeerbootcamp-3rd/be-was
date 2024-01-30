package util;

import model.Parameter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class URIParser {
    public static boolean isResourceURI(String uri) {
        return uri.contains(".css")
                || uri.contains("fonts")
                || uri.contains(".images")
                || uri.contains(".js")
                || uri.contains(".ico")
                || uri.contains(".html");
    }

    public static String extractType(String uri) {
        int start = uri.lastIndexOf(".");
        return uri.substring(start + 1);
    }

    public static boolean hasQuery(String uri) {
        return uri.contains("?");
    }

    public static String getUri(String uriWithQuery) {
        return uriWithQuery.split("\\?")[0];
    }

    public static String getQuery(String uriWithQuery) {
        return uriWithQuery.split("\\?")[1];
    }

    public static Parameter getParamMap(String query) throws UnsupportedEncodingException {
        Parameter paramMap = new Parameter();

        query = URLDecoder.decode(query, "UTF-8");
        String[] params = query.split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            paramMap.put(keyValue[0], keyValue[1]);
        }

        return paramMap;
    }

    public static String extractStaticResourcePath(String uri) {
        String[] uriToken = uri.split("/");
        for (int i = 0; i < uriToken.length; i++) {
            if (isStaticResourceType(uriToken[i])) {
                if (uriToken[i].equals("favicon.ico")) {
                    return "/" + uriToken[i];
                }
                return "/" + uriToken[i] + "/" + uriToken[i + 1];
            }
        }
        return "";
    }

    public static boolean isStaticResourceType(String type) {
        return type.contains("css")
                || type.contains("fonts")
                || type.contains("images")
                || type.contains("js")
                || type.contains("favicon.ico");
    }
}
