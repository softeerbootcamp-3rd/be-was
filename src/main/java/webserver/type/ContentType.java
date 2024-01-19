package webserver.type;

public enum ContentType {
    HTML("text/html");
    private final String value;

    ContentType(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
