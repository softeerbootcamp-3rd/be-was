package http.response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private int statusCode;
    private Map<String, String> headers;
    private byte[] body;

    public Map<String, String> getHeaders() {
        return headers;
    }

    public HttpResponse() {
        this.headers = new HashMap<>();
    }

    //getter
    public byte[] getBody() {
        return body;
    }
    public int getStatusCode() {
        return statusCode;
    }

    //setter
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setHeader(String key, String value) {
        headers.put(key, value);
    }

    public void setBody(byte[] body) {
        this.body = body;
        if (body != null) {
            this.headers.put("Content-Length", String.valueOf(body.length));
        }
    }

    public void send(DataOutputStream dos) throws IOException {
        dos.writeBytes("HTTP/1.1 " + statusCode + " \r\n");
        // Ensure Content-Type is set
        headers.putIfAbsent("Content-Type", "text/plain");
        for (Map.Entry<String, String> header : headers.entrySet()) {
            dos.writeBytes(header.getKey() + ": " + header.getValue() + "\r\n");
        }
        dos.writeBytes("\r\n");
        if (body != null) {
            dos.write(body, 0, body.length);
        }
        dos.flush();
    }
}
