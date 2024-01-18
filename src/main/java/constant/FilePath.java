package constant;

public enum FilePath {
    BASE_PATH ("src/main/resources/templates"),
    MAIN_PAGE ("/index.html");

    private final String path;

    FilePath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
