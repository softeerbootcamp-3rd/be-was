package response;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private String version;
    private String statusCode;
    private String statusMessage;
    private Map<String, String> headers = new HashMap<>();
    private byte[] body;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }



    public void setResponse(String statusCode, String statusMessage, byte[] body, Map<String, String> headers) {
        this.version = "HTTP/1.1";
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.body = body;
        for (String key : headers.keySet()) {
            this.headers.put(key, headers.get(key));
        }
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "version='" + version + '\'' +
                ", statusCode='" + statusCode + '\'' +
                ", statusMessage='" + statusMessage + '\'' +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }
}
