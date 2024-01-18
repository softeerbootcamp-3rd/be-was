package constant;

public enum FilePath {
    HTML_BASE ("src/main/resources/templates"),
    SUPPORT_FILE_BASE ("src/main/resources/static"),
    MAIN_PAGE ("/index.html");

    private final String path;

    FilePath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
