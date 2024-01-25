package http;

import http.builder.HttpRequestBuilder;
import util.HttpRequestHeaderUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpRequestFactory {

    public static HttpRequest getRequest(InputStream in) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String requestHeader = getRequestHeader(br);
		HttpRequestHeaderUtils httpRequestHeaderUtils = HttpRequestHeaderUtils.createHeaderUtils(requestHeader);
		String requestBody = getRequestBody(br, httpRequestHeaderUtils);

		return new HttpRequestBuilder()
                .method(httpRequestHeaderUtils.getRequestMethod())
                .uri(httpRequestHeaderUtils.getRequestUri())
                .queryString(httpRequestHeaderUtils.getQueryString())
                .headerFields(httpRequestHeaderUtils.getRequestHeaders())
				.body(requestBody)
                .build();
    }

	private static String getRequestHeader(BufferedReader br) throws IOException { //헤더를 읽는 메서드로 변환
        StringBuilder sb = new StringBuilder();
		String line = br.readLine();
		sb.append(line).append("\r\n");
		while (!line.isEmpty()) { //마지막 라인 = ""(empty)
			line = br.readLine();
			sb.append(line).append("\r\n");
		}
		return URLDecoder.decode(sb.toString().trim(), UTF_8);
    }

	private static String getRequestBody(BufferedReader br, HttpRequestHeaderUtils httpRequestHeaderUtils) throws IOException {
		Map<String, String> fieldMap = httpRequestHeaderUtils.getRequestHeaders();
		String contentLength = fieldMap.get("Content-Length");
		if (contentLength != null) {
			char[] buf = new char[Integer.parseInt(contentLength)];
			br.read(buf);
			return URLDecoder.decode(String.valueOf(buf), UTF_8);
		}
		return ""; // body 없는 경우
	}
}
