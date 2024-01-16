package model.http;

public class Body {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "{" +
                "content='" + content + '\'' +
                '}' + "\n";
    }
}
