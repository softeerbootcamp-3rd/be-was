package model.http;

public enum ContentType {
    // TEXT 타입
    CSS("text/css"), HTML("text/html"), JAVASCRIPT("text/javascript"), PLAIN("text/plain"), XML("text/xml"), MIME("application/font-sfnt");

    private final String type;

    ContentType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
