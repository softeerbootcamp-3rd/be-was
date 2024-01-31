package http;

import http.builder.HttpResponseBuilder;
import http.method.HttpMethod;
import util.UriParser;
import util.mapper.ContentTypeMapper;

import java.util.HashMap;
import java.util.Map;

import static http.status.HttpStatusCode.*;

public class HttpResponseFactory {
    public static HttpResponse get200Response(String uri, byte[] body) {
        Map<String, String> headerFields = setHeader(uri, body);
        return new HttpResponseBuilder()
                .status(OK)
                .headerFields(headerFields)
                .body(body)
                .build();
    }
    public static HttpResponse get302Response(String uri) {
        Map<String, String> headerFields = set302Header(uri);
        return new HttpResponseBuilder()
                .status(FOUND)
                .headerFields(headerFields)
				.body(new byte[0])
                .build();
    }
	public static HttpResponse get302LoginResponse(String uri, String sessionId) {
		Map<String, String> headerFields = set302LoginHeader(uri, sessionId);
		return new HttpResponseBuilder()
			.status(FOUND)
			.headerFields(headerFields)
			.body(new byte[0])
			.build();
	}

	public static HttpResponse get400Response(String uri, byte[] body) {
		Map<String, String> headerFields = setHeader(uri, body);
		return new HttpResponseBuilder()
			.status(BAD_REQUEST)
			.headerFields(headerFields)
			.body(body)
			.build();
	}
	public static HttpResponse get404Response(String uri, byte[] body) {
		Map<String, String> headerFields = setHeader(uri, body);
		return new HttpResponseBuilder()
			.status(NOT_FOUND)
			.headerFields(headerFields)
			.body(body)
			.build();
	}

	public static HttpResponse get405Response(String uri, byte[] body, HttpMethod requiredMethod) {
		Map<String, String> headerFields = set405Header(uri, body, requiredMethod);
		return new HttpResponseBuilder()
			.status(METHOD_NOT_ALLOWED)
			.headerFields(headerFields)
			.body(body)
			.build();
	}

	private static Map<String, String> set405Header(String uri, byte[] body, HttpMethod requiredMethod) {
		Map<String, String> headerFields = new HashMap<>();
		String fileType = UriParser.getFileType(uri);
		String contentType = ContentTypeMapper.getContentType(fileType);
		headerFields.put("Content-Type", contentType + ";charset=utf-8");
		headerFields.put("Content-Length", String.valueOf(body.length));
		headerFields.put("Allow", requiredMethod.toString());
		return headerFields;
	}


    private static Map<String, String> setHeader(String uri, byte[] body) {
        Map<String, String> headerFields = new HashMap<>();
		String fileType = UriParser.getFileType(uri);
		String contentType = ContentTypeMapper.getContentType(fileType);
        headerFields.put("Content-Type", contentType + ";charset=utf-8");
        headerFields.put("Content-Length", String.valueOf(body.length));
        return headerFields;
    }
    private static Map<String, String> set302Header(String uri){
        Map<String, String> headerFields = new HashMap<>();
        headerFields.put("Location", uri);
        return headerFields;
    }
	private static Map<String, String> set302LoginHeader(String uri, String sessionId){
		Map<String, String> headerFields = new HashMap<>();
		headerFields.put("Location", uri);
		setCookie(sessionId, headerFields);
		return headerFields;
	}
	private static void setCookie(String sessionId, Map<String, String> headerFields) {
		String cookie = "SID=" + sessionId + "; Path=/";
		headerFields.put("Set-Cookie", cookie);
	}
}
