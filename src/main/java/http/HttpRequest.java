package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static http.HttpVersionConst.*;

public class HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);
    private final String method;
    private final String uri;
    private final String queryString; // 없는 경우 None
    private final Map<String, String> headerFields;
	private final String body; // 없는 경우 "" (empty)

    public HttpRequest(String method, String uri, String queryString, Map<String, String> headerFields, String body) {
        this.method = method;
        this.uri = uri;
        this.queryString = queryString;
        this.headerFields = headerFields;
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getQueryString() {
        return queryString;
    }

    public Map<String, String> getHeaderFields() {
        return headerFields;
    }

	public String getBody() {
		return body;
	}

	public void logHeaders(){ // 바디도 같이 찍기
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("Method: ").append(method).append("\n");
        sb.append("Uri: ").append(uri).append("\n");
        sb.append("Query-String: ").append(queryString).append("\n");
        sb.append("Version: ").append(VERSION).append("\n");
        for (Map.Entry<String, String> entry : headerFields.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
		sb.append("Body: ").append(body);
		logger.debug("{}", sb.toString().trim());
	}
}
