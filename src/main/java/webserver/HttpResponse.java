package webserver;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private static final String HTTP_VERSION = "HTTP/1.1";
    private final int statusCode;
    private String statusText;
    private Map<String, String> headers = new HashMap<>();
    private byte[] body;

    public HttpResponse(int statusCode){
        this.statusCode = statusCode;
        setStatusText(statusCode);
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void setStatusText(int statusCode) {
        switch (statusCode) {
            case 200: statusText = "OK";
                break;
            case 404: statusText = "Not Found";
                break;
            default: break;
        }
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public String getHttpVersion(){
        return HTTP_VERSION;
    }

    public int getStatusCode(){
        return statusCode;
    }

    public String getStatusText(){
        return statusText;
    }

    public Map<String, String> getHeaders(){
        return headers;
    }

    public byte[] getBody(){
        return body;
    }
}
