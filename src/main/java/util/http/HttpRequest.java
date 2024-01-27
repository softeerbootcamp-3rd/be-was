package util.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);
    private RequestEntity<?> request;

    private HttpRequest() {}

    public static HttpRequest parse(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        HttpRequest httpRequest = new HttpRequest();

        String line = br.readLine();
        String[] tokens = line.split(" ");
        HttpMethod method = HttpMethod.valueOf(tokens[0]);
        URI url = URI.create(tokens[1]);

        String accept = MediaType.ALL_VALUE;
        String cookie = null;
        String contentLength = null;
        String contentType = null;

        logger.debug(line);
        while (!(line = br.readLine()).isEmpty()) {
            tokens = line.split(":");
            if ("Accept".equalsIgnoreCase(tokens[0])) {
                accept = tokens[1].split(",")[0].trim();
                logger.debug(line);
            }
            if ("Cookie".equalsIgnoreCase(tokens[0])) {
                cookie = tokens[1].trim();
                logger.debug(line);
            }
            if ("Content-Length".equalsIgnoreCase(tokens[0])) {
                contentLength = tokens[1].trim();
                logger.debug(line);
            }
            if ("Content-Type".equalsIgnoreCase(tokens[0])) {
                contentType = tokens[1].trim();
                logger.debug(line);
            }
            if ("Host".equalsIgnoreCase(tokens[0]) || "Connection".equalsIgnoreCase(tokens[0]))
                logger.debug(line);
        }

        char[] body = null;
        if (contentLength != null) {
            body = new char[Integer.parseInt(contentLength)];
            br.read(body);
        }

        httpRequest.request = RequestEntity.method(method, url)
                .header(HttpHeaders.ACCEPT, accept)
                .header(HttpHeaders.COOKIE, cookie)
                .header(HttpHeaders.CONTENT_LENGTH, contentLength)
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(body == null ? null : new String(body));

        return httpRequest;
    }


    public String getPath() {
        return this.request.getUrl().getPath();
    }

    public String getHeader(String headerName) {
        return this.request.getHeaders().getFirst(headerName);
    }

    public Map<String, String> getQueryMap() {
        Map<String, String> queryMap = new HashMap<>();

        String query = this.request.getUrl().getQuery();
        String[] params = query.split("&");

        for (String s : params) {
            String[] keyVal = s.split("=");
            String key = keyVal[0];
            String value = null;
            if (keyVal.length == 2)
                value = keyVal[1];
            queryMap.put(key, value);
        }
        return queryMap;
    }

    public Map<String, String> getBodyParams() {
        Map<String, String> queryMap = new HashMap<>();

        String body = (String)this.request.getBody();

        String[] params = body.split("&");

        for (String s : params) {
            String[] keyVal = s.split("=");
            String key = keyVal[0];
            String value = null;
            if (keyVal.length == 2)
                value = keyVal[1];
            queryMap.put(key, value);
        }
        return queryMap;
    }

    public Map<String, String> getCookieMap() {
        Map<String, String> cookieMap = new HashMap<>();

        String cookie = getHeader(HttpHeaders.COOKIE);

        String[] tokens = cookie.split(";");

        for (String s : tokens) {
            String[] keyVal = s.split("=");
            cookieMap.put(keyVal[0].trim(), keyVal[1].trim());
        }
        return cookieMap;
    }
}
