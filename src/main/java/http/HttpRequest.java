package http;

import http.header.RequestHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.WebServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequest {
    private final String url;
    private final String method;
    private Map<String, String> queryString;
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);
    private RequestHeader requestHeader;

    private HttpRequest(String method, String url, Map<String, String> queryString, RequestHeader requestHeader) {
        this.method = method;
        this.url = url;
        this.queryString = queryString;
        this.requestHeader = requestHeader;
    }

    public static HttpRequest createFromReader(BufferedReader reader) throws IOException {
        String request = reader.readLine().trim();
        String[] httpRequestTokens = request.split(" ");

        String method = httpRequestTokens[0];
        String url = httpRequestTokens[1];
        Map<String, String> queryString = new HashMap<>();

        String[] url_split = url.split("\\?");

        url = url_split[0];

        if (url_split.length > 1) {
            String[] queryStringArray = url_split[1].split("&");

            for (String queryKeyValue : queryStringArray) {
                String[] query_split = queryKeyValue.split("=");
                if (query_split.length != 2 || query_split[1].isBlank())
                    throw new IOException("Incorrect Query String");

                query_split[1] = URLDecoder.decode(query_split[1], StandardCharsets.UTF_8);
                queryString.put(query_split[0], query_split[1]);
            }
        }
        RequestHeader header = RequestHeader.createFromReader(reader);

        return new HttpRequest(method, url, queryString, header);
    }


    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, String> getQueryString() {
        return queryString;
    }

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    @Override
    public String toString() {
        String query = queryString.entrySet().stream()
                .map(e -> e.getKey() + ":" + e.getValue())
                .collect(Collectors.joining(" | "));

        return "HttpRequest(method = " + method + ",url = " +
                url + ", queryString = {" + query +
                "}, header = {" + requestHeader.toString() + "})";
    }
}
