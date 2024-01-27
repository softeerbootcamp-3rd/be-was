package constant;

public enum StaticFile {
    HTML_BASE ("src/main/resources/templates"),
    SUPPORT_FILE_BASE ("src/main/resources/static"),
    MAIN_PAGE ("/index.html");

    public final String path;

    StaticFile(String path) {
        this.path = path;
    }
}
