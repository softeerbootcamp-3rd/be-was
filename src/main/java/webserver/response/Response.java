package webserver.response;

import webserver.status.HttpStatus;
import webserver.type.ContentType;

import java.util.HashMap;
import java.util.Map;


public class Response {
    private final HttpStatus httpStatus;
    private final Map<String, String> headers;
    private final byte[] body;

    private Response(HttpStatus httpStatus, byte[] body){
        this.httpStatus = httpStatus;
        this.headers = new HashMap<>();
        this.body = body;
    }

    public String getHeaders(){
        StringBuilder sb = new StringBuilder();

        sb.append("HTTP/1.1 " + httpStatus.getCode() + " " + httpStatus.getName() + "\r\n");
        headers.forEach((key, value) -> sb.append(key).append(": ").append(value).append("\r\n"));
        sb.append("\r\n");

        return sb.toString();
    }

    public byte[] getBody() {
        return body;
    }

    public boolean existsBody(){
        return body != null;
    }

    public static Response redirect(String location){
        return new Response(HttpStatus.FOUND, null)
                .setHeader("Location", location);
    }

    public static Response onSuccess(byte[] body){
        return new Response(HttpStatus.OK, body)
                .setHeader("Content-Length", String.valueOf(body.length))
                .setHeader("Content-Type", ContentType.HTML.getValue());
    }

    public static Response onSuccess(byte[] body, ContentType contentType){
        return new Response(HttpStatus.OK, body)
                .setHeader("Content-Length", String.valueOf(body.length))
                .setHeader("Content-Type", contentType.getValue());
    }

    public static Response onFailure(HttpStatus httpStatus, byte[] body){
        return new Response(httpStatus, body)
                .setHeader("Content-Length", String.valueOf(body.length))
                .setHeader("Content-Type", ContentType.HTML.getValue());
    }

    private Response setHeader(String key, String value){
        headers.put(key, value);

        return this;
    }
}
