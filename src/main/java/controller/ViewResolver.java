package controller;

public class ViewResolver {
    public static View resolveViewName(String viewName) {
        if (viewName.contains(".html")) {
            return new View("./src/main/resources/templates" + viewName);
        }

        viewName = extractStaticResourcePath(viewName);
        return new View("./src/main/resources/static" + viewName);
    }

    private static String extractStaticResourcePath(String uri) {
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

    private static boolean isStaticResourceType(String type) {
        return type.contains("css")
                || type.contains("fonts")
                || type.contains("images")
                || type.contains("js")
                || type.contains("favicon.ico");
    }
}
