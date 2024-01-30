package controller;

public enum ResourceMapping {
    HTML(ResourceConstants.TEMPLATE_DIRECTORY, "text/html"),
    TXT(ResourceConstants.UPLOADS_URL, "text/plain"),
    CSS(ResourceConstants.STATIC_DIRECTORY, "text/css"),
    JS(ResourceConstants.STATIC_DIRECTORY, "application/javascript"),
    ICO(ResourceConstants.STATIC_DIRECTORY, "image/x-icon"),
    WOFF(ResourceConstants.STATIC_DIRECTORY, "font/woff"),
    TTF(ResourceConstants.STATIC_DIRECTORY, "font/ttf"),
    PNG(ResourceConstants.UPLOADS_URL, "image/png"),
    JPG(ResourceConstants.UPLOADS_URL, "image/jpg"),
    JPEG(ResourceConstants.UPLOADS_URL, "image/jpeg");


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
        public static final String UPLOADS_URL = "/uploads";
    }
}
