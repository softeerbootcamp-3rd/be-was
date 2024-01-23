package webserver.http;

public enum ContentType {
    APPLICATION_JSON("application/json"),
    APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded"),
    NULL("");

    private final String type;

    ContentType(String type) {
        this.type = type;
    }

    public String getType(){
        return type;
    }

    public static ContentType convertContentType(String ext) {
        for (ContentType contentType : ContentType.values()) {
            if (contentType.getType().equals(ext)) {
                return contentType;
            }
        }
        return ContentType.NULL;
    }
}
