package webserver.http.response;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private static final String HTTP_VERSION = "HTTP/1.1";
    private final HttpStatus status;
    private Map<String, String> headers = new HashMap<>();
    private byte[] body;

    public HttpResponse(HttpStatus httpStatus){
        status = httpStatus;
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public String getHttpVersion(){
        return HTTP_VERSION;
    }

    public int getStatusCode(){
        return status.getCode();
    }

    public String getStatusText(){
        return status.getText();
    }

    public Map<String, String> getHeaders(){
        return headers;
    }

    public byte[] getBody(){
        return body;
    }
}
