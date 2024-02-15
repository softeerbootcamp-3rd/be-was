package controller;

import util.URIParser;

public class ViewResolver {
    public static View resolveViewName(String viewName) {
        if (viewName.contains(".html")) {
            return new View("./src/main/resources/templates" + viewName);
        }

        viewName = URIParser.extractStaticResourcePath(viewName);
        return new View("./src/main/resources/static" + viewName);
    }

}
