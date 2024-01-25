package http;

import http.builder.HttpResponseBuilder;
import http.status.HttpStatusCode;
import util.mapper.ContentTypeMapper;

import java.util.HashMap;
import java.util.Map;

public class HttpResponseFactory {
    public static HttpResponse getHttpResponse(HttpStatusCode statusCode, String fileType, byte[] body) {
        Map<String, String> headerFields = setHeaderFields(fileType, body);
        return new HttpResponseBuilder()
                .status(statusCode)
                .headerFields(headerFields)
                .body(body)
                .build();
    }

    public static HttpResponse get302HttpResponse(String uri) {
        Map<String, String> headerFields = setRedirectionHeader(uri);
        return new HttpResponseBuilder()
                .status(HttpStatusCode.FOUND)
                .headerFields(headerFields)
				.body(new byte[0])
                .build();
    }

	public static HttpResponse get302HttpResponse(String uri, String sessionId) {
		Map<String, String> headerFields = setRedirectionHeader(uri, sessionId);
		return new HttpResponseBuilder()
			.status(HttpStatusCode.FOUND)
			.headerFields(headerFields)
			.body(new byte[0])
			.build();
	}

	private static Map<String, String> setRedirectionHeader(String uri, String sessionId){
		Map<String, String> headerFields = new HashMap<>();
		headerFields.put("Content-Type", "text/html;charset=utf-8");
		headerFields.put("Location", uri);
		headerFields.put("Set-Cookie", "SID=" + sessionId + "; Path=/");
		return headerFields;
	}

    private static Map<String, String> setHeaderFields(String fileType, byte[] body) {
        Map<String, String> headerFields = new HashMap<>();
		String contentType = ContentTypeMapper.getContentType(fileType);
        headerFields.put("Content-Type", contentType + ";charset=utf-8");
        headerFields.put("Content-Length", String.valueOf(body.length));
        return headerFields;
    }

    private static Map<String, String> setRedirectionHeader(String uri){
        Map<String, String> headerFields = new HashMap<>();
        headerFields.put("Content-Type", "text/html;charset=utf-8");
        headerFields.put("Location", uri);
        return headerFields;
    }
}
