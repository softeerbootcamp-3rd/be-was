package model.http;

public class Body {
    private final byte[] content;

    public Body(byte[] content) {
        this.content = content;
    }

    public byte[] getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Body{" +
                "content=" + new String(content) +
                '}';
    }
}