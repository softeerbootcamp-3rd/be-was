package controller;

public class ResourceController {
    private String type;

    public ResourceController(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public ModelView process(String uri) {
        uri = getResourcePath(uri);
        return new ModelView("/static" + uri);
    }

    private String getResourcePath(String uri) {
        String[] uriToken = uri.split("/");
        for (int i = 0; i < uriToken.length; i++) {
            if (isResourceType(uriToken[i])) {
                if (uriToken[i].equals("favicon.ico")) {
                    return "/" + uriToken[i];
                }
                return "/" + uriToken[i] + "/" + uriToken[i + 1];
            }
        }
        return "";
    }

    private boolean isResourceType(String type) {
        return type.contains("css")
                || type.contains("fonts")
                || type.contains("images")
                || type.contains("js")
                || type.contains("favicon.ico");
    }
}
