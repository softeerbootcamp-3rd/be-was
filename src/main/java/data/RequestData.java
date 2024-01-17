package data;

import java.util.Map;

public class RequestData {
    private final String method;
    private final String requestContent;
    private final String httpVersion;
    private final Map<String, String> headers;

    public RequestData(String method, String requestContent, String httpVersion, Map<String, String> headers) {
        this.method = method;
        this.requestContent = requestContent;
        this.httpVersion = httpVersion;
        this.headers = headers;
    }

    public String getMethod() {
        return method;
    }

    public String getRequestContent() {
        return requestContent;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getHeaderValue(String headerName) {
        return headers.get(headerName);
    }

    public String formatForOutput() {
        StringBuilder output = new StringBuilder();
        output.append("\n===\n");
        output.append("Method: ").append(method).append("\n");
        output.append("Request Content: ").append(requestContent).append("\n");
        output.append("HTTP Version: ").append(httpVersion).append("\n===\n");

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            output.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        output.append("===\n");

        return output.toString();
    }
}
