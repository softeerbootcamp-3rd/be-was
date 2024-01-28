package controller;

public class ResourceController {
    private String type;

    public ResourceController(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public ModelView process(String uri) { // todo 해당 역할은 viewResolver에서 일어나도록 리팩토링
        if (isTemplatesResourceType(uri)) {
            return new ModelView("/templates" + uri);
        }

        uri = getStaticResourcePath(uri);
        return new ModelView("/static" + uri);
    }

    private String getStaticResourcePath(String uri) {
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

    private boolean isStaticResourceType(String type) {
        return type.contains("css")
                || type.contains("fonts")
                || type.contains("images")
                || type.contains("js")
                || type.contains("favicon.ico");
    }

    private boolean isTemplatesResourceType(String type) {
        return type.contains(".html");
    }
}
