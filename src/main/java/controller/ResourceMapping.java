package controller;

public enum ResourceMapping {
    HTML("/templates", "text/html"),
    CSS("/static", "text/css"),
    JS("/static", "application/javascript"),
    ICO("/static", "image/x-icon"),
    PNG("/static", "image/png"),
    JPG("/static", "image/jpg"),
    WOFF("/static", "font/woff"),
    TTF("/static", "font/ttf");

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
}
