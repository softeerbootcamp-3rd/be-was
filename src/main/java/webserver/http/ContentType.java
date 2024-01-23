package webserver.http;

public enum ContentType {
    APPLICATION_JSON("application/json"),
    APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded");

    private final String type;

    ContentType(String type) {
        this.type = type;
    }

    public String getType(){
        return type;
    }
}
