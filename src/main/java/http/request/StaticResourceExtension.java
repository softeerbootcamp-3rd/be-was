package http.request;

public enum StaticResourceExtension {
    HTML(".html"),
    CSS(".css"),
    JS(".js"),
    ICO(".ico")
    ;

    private String extension;

    StaticResourceExtension(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}
