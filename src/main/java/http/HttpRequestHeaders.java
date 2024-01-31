package http;

import java.util.Map;
import java.util.Set;

public class HttpRequestHeaders {

	private final Map<String, String> requestHeaderMap;

	public HttpRequestHeaders(Map<String, String> requestHeaderMap) {
		this.requestHeaderMap = requestHeaderMap;
	}

	public String getHeader(String field) {
		return requestHeaderMap.get(field);
	}

	public Set<Map.Entry<String, String>> getAllHeader() {
		return requestHeaderMap.entrySet();
	}

}
