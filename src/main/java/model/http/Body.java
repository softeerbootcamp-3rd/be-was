package model.http;

public class Body {
    private final String content;

    public Body(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "{" +
                "content='" + content + '\'' +
                '}' + "\n";
    }
}
