package dto;

public class RequestBuilder<T> {

    private String path;
    private T requestBody;

    public RequestBuilder(String path, T requestBody) {
        this.path = path;
        this.requestBody = requestBody;
    }

    public String getPath() {
        return path;
    }

    public T getRequestBody() {
        return requestBody;
    }
}
