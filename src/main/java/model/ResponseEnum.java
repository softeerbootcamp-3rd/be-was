package model;

public enum ResponseEnum {
    DEFAULT("html", "src/main/resources/templates", "text/html"),
    CSS("css", "src/main/resources/static", "text/css"),
    JS("js", "src/main/resources/static","application/javascript"),
    FONT("fonts", "src/main/resources/static","font/ttf"),
    IMAGE("images","src/main/resources/static" , "image/*"),
    ICON("ico", "src/main/resources/static", "image/x-icon");
    private final String extension;
    private final String pathName;
    private final String contentType;

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
        return DEFAULT.pathName;
    }

    public static String getContentType(String extension) {
        for (ResponseEnum responseEnum : values()) {
            if (responseEnum.extension.equals(extension)) {
                return responseEnum.contentType;
            }
        }
        return DEFAULT.contentType;
    }
}
