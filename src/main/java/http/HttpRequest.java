package http;

import http.method.HttpMethod;

import java.util.Map;

public class HttpRequest {
    private final HttpMethod method;
    private final String uri;
    private final String queryString; // 없는 경우 None
	private final String version;
    private final HttpRequestHeaders requestHeaders;
	private final String body; // 없는 경우 "" (empty)

    public HttpRequest(HttpMethod method, String uri, String queryString, Map<String, String> headerFields, String body) {
        this.method = method;
        this.uri = uri;
        this.queryString = queryString;
		this.version = "HTTP/1.1";
		this.requestHeaders = new HttpRequestHeaders(headerFields);
        this.body = body;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getQueryString() {
        return queryString;
    }

	//쿠키 필드가 없으면 빈 문자열 배열 반환
	public String[] getCookies() {
		String cookieValues = requestHeaders.getHeader("cookie");
		if (cookieValues == null) {
			return new String[0];
		}
		return cookieValues.split(";");
	}
	public String getBody() {
		return body;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("method: ").append(method.toString()).append("\n");
		sb.append("uri: ").append(uri).append("\n");
		sb.append("query-string: ").append(queryString).append("\n");
		sb.append("version: ").append(version).append("\n");
		for (Map.Entry<String, String> entry : requestHeaders.getAllHeader()) {
			sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}
		sb.append("body: ").append(body);
		return sb.toString().trim();
	}
}
