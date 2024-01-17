package model;

import java.util.Map;

public class HttpHeader {
    private HttpStatusCode statusCode;
    private Map<String, String> properties;

    public HttpHeader(HttpStatusCode statusCode, Map<String, String> properties) {
        this.statusCode = statusCode;
        this.properties = properties;
    }

    public String getProperty(String property) throws RuntimeException {
        String result = properties.getOrDefault(property, null);
        if (result == null)
            throw new RuntimeException("No Such Property in HttpHeader");
        return result;
    }

    @Override
    public String toString() throws RuntimeException {
        String result = String.format("HTTP/1.1 %d %s\r\n", statusCode.getCode(), statusCode.getMessage());

        for (Map.Entry<String, String> entry : properties.entrySet()) {
            result += String.format("%s: %s\r\n", entry.getKey(), entry.getValue());
        }

        result += "\r\n";
        return result;
    }
}
