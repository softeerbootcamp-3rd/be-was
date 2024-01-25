package http;

import http.status.HttpStatusCode;

import java.util.Map;

import static http.HttpVersionConst.*;

public class HttpResponse {
    private final String version;
    private final HttpStatusCode statusCode;
    private final Map<String, String> headerFields;
    private final byte[] body; // 없는 경우 빈 배열 (body = new byte[0])

    public HttpResponse(HttpStatusCode statusCode, Map<String, String> headerFields, byte[] body) {
        this.version = VERSION;
        this.statusCode = statusCode;
        this.headerFields = headerFields;
        this.body = body;
    }

    public String getVersion() {
        return version;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getHeaderFields() {
        return headerFields;
    }

    public byte[] getBody() {
        return body;
    }

    public String getResponseHeader() {
        StringBuilder sb = new StringBuilder();
        String responseLine = version + " " + statusCode + "\r\n";
        sb.append(responseLine);
        for (Map.Entry<String, String> line : headerFields.entrySet()) {
            sb.append(line.getKey()).append(": ").append(line.getValue()).append("\r\n");
        }
        sb.append("\r\n");
        return sb.toString();
    }
}
