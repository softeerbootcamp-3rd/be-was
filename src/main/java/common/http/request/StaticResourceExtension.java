package common.http.request;

public enum StaticResourceExtension {
    HTML(".html"),
    CSS(".css"),
    JS(".js"),
    ICO(".ico"),
    WOFF(".woff"),
    TTF(".ttf"),
    PNG(".png"),
    ;

    private String extension;

    StaticResourceExtension(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}
