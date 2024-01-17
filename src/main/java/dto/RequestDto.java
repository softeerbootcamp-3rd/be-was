package dto;

import util.HttpStatus;

public class RequestDto<T> {

    private String path;
    private T requestBody;

    public RequestDto(String path, T requestBody) {
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
