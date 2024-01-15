package webserver;

public enum ResponseEnum {
    HTML("index.html", "src/main/resources/templates", "text/html"),
    CSS("css", "src/main/resources/static", "text/css"),
    JS("js", "src/main/resources/static","application/javascript"),
    FONTS("fonts", "src/main/resources/static","font/ttf");
    private final String extension;
    private final String pathName;
    private final String contentType;

    private static final String NONE = "없음";

    ResponseEnum(String extension, String pathName, String contentType) {
        this.extension = extension;
        this.pathName = pathName;
        this.contentType = contentType;
    }

    public static String getPathName(String extension) {
        for (ResponseEnum responseEnum : values()) {
            if (responseEnum.extension.equals(extension)) {
                return responseEnum.pathName;
            }
        }
        return NONE;
    }

    public static String getContentType(String extension) {
        for (ResponseEnum responseEnum : values()) {
            if (responseEnum.extension.equals(extension)) {
                return responseEnum.contentType;
            }
        }
        return NONE;
    }

}
