package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.*;

import static constant.HttpRequestConstant.*;

public class HttpRequest {
    private final String method;
    private final URI uri;
    private final Map<String, String>  headers;
    private final String body;

    public HttpRequest(String method, URI uri, Map<String, String> headers, String body) {
        this.method = Objects.requireNonNull(method);
        this.uri = Objects.requireNonNull(uri);
        this.headers = Objects.requireNonNull(headers);
        this.body = body;
    }

    public static HttpRequest from(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line = br.readLine();
        String[] startLine = line.split(PATH_DELIMITER);

        String method = startLine[HTTP_METHOD_POS];
        URI uri = URI.create(startLine[PATH_POS]);

        Map<String, String> headers = getHeaders(br);

        if(method.equals("POST")){
            int contentLength = Integer.parseInt(headers.get(CONTENT_LENGTH));
            char[] body = new char[contentLength];
            return new HttpRequest(method, uri, headers, new String(body, 0, br.read(body)));
        }

        return new HttpRequest(method, uri, headers, null);
    }

    public String getExtension() {
        String[] split = getPath().split(SLASH_DELIMITER)[EXTENSION_POS].split(DOT_DELIMITER);
        //TODO 3항 연산자 안쓰도록 수정
        return split.length > 1 ? split[1] : split[0];
    }

    public String getSessionId(){
        String cookieHeader = headers.get(COOKIE);
        if(cookieHeader == null){
            return null;
        }
        return cookieHeader.split(SID_DELIMITER)[SID_POS];
    }

    public URI getUri() {
        return uri;
    }
    public String getMethod() {
        return method;
    }
    public String getPath() {
        return uri.getPath();
    }
    public String getBody() {
        return body;
    }

    private static Map<String, String> getHeaders(BufferedReader br) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String line;

        while (!(line = br.readLine()).equals(END)) {
            String[] header = line.split(HEADER_DELIMITER);
            if (line.startsWith(HOST) || line.startsWith(CONNECTION) || line.startsWith(ACCEPT) || line.startsWith(CONTENT_LENGTH) || line.startsWith(COOKIE)) {
                headers.put(header[KEY_INDEX], header[VALUE_INDEX]);
            }
        }

        return headers;
    }

    @Override
    public String toString() {
        return "HttpRequest{" + "\n" +
                "method=" + method + ",\n" +
                "uri=" + uri + ",\n" +
                "headers=" + headers + ",\n" +
                "body=" + body + "\n" +
                '}';
    }
}
