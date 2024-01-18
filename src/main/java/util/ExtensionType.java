package util;

public enum ExtensionType {
    HTML("text/html"),
    CSS("text/css"),
    JS("text/javascript"),
    PNG("image/png"),
    ICO("image/x-icon"),
    SVG("image/svg+xml"),
    WOFF("font/woff"),
    TTF("font/ttf"),
    EOT("font/eot"),
    WOFF2("font/woff2");


    private String extension;

    ExtensionType(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
}
