package controller;

public enum ResourceMapping {
    HTML(ResourceConstants.TEMPLATE_DIRECTORY, "text/html"),
    CSS(ResourceConstants.STATIC_DIRECTORY, "text/css"),
    JS(ResourceConstants.STATIC_DIRECTORY, "application/javascript"),
    ICO(ResourceConstants.STATIC_DIRECTORY, "image/x-icon"),
    PNG(ResourceConstants.STATIC_DIRECTORY, "image/png"),
    JPG(ResourceConstants.STATIC_DIRECTORY, "image/jpg"),
    WOFF(ResourceConstants.STATIC_DIRECTORY, "font/woff"),
    TTF(ResourceConstants.STATIC_DIRECTORY, "font/ttf");

    private final String directory;
    private final String contentType;

    ResourceMapping(String directory, String contentType) {
        this.directory = directory;
        this.contentType = contentType;
    }

    public String getDirectory() {
        return directory;
    }
    public String getContentType() {
        return contentType;
    }

    public static class ResourceConstants {
        public static final String TEMPLATE_DIRECTORY = "/templates";
        public static final String STATIC_DIRECTORY = "/static";
    }
}
