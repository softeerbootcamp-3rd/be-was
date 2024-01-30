package constant;

public enum HeaderType {
    LOCATION("Location"),
    CONTENT_TYPE("Content-Type"),
    SET_COOKIE("Set-Cookie"),
    COOKIE("Cookie");

    String type;

    HeaderType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
