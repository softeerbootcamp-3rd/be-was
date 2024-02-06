package http.request.parser;

import http.request.HttpRequest;
import java.util.Map;
import java.util.StringTokenizer;

public class HttpRequestParser {
    public static void parse(HttpRequest request, String requestText) {
        StringTokenizer tokenizer = new StringTokenizer(requestText, "\n");

        // 요청 라인 파싱
        if (tokenizer.hasMoreTokens()) {
            RequestLineParser.parse(request, tokenizer.nextToken());
        }

        // 헤더 파싱
        RequestHeaderParser.parse(request, tokenizer);

        // 쿠키 파싱
        String cookieHeader = request.getHeaders().get("Cookie");
        Map<String, String> cookies = RequestCookieParser.parseCookies(cookieHeader);
        request.setCookies(cookies);

        // 요청 바디 파싱
        if (tokenizer.hasMoreTokens()) {
            String body = tokenizer.nextToken();
            RequestBodyParser.parse(request, body);
        }
    }
}
