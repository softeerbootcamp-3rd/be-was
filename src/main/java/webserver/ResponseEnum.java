package webserver;

public enum ResponseEnum {
    CSS("css", "src/main/resources/static", "text/css"),
    JS("js", "src/main/resources/static","application/javascript"),
    FONT("fonts", "src/main/resources/static","font/ttf"),
    IMAGE("images","src/main/resources/static" , "image/");
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
        return "src/main/resources/templates";
    }

    public static String getContentType(String extension) {
        for (ResponseEnum responseEnum : values()) {
            if (responseEnum.extension.equals(extension)) {
                return responseEnum.contentType;
            }
        }
        return "text/html";
    }

}
