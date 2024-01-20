package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static constant.HttpRequestConstant.*;

public class HttpRequest {
    private final String method;
    private final URI uri;
    private final String host;
    private final String connection;
    private final String accept;

    private static final int KEY_INDEX = 0;
    private static final int VALUE_INDEX = 1;
    public static final int EXTENSION_POS = 1;
    public static final String SLASH_DELIMITER = "/";
    public static final String DOT_DELIMITER = "\\.";

    public HttpRequest(String method, URI uri, String host, String connection, String accept) {
        this.method = Objects.requireNonNull(method);
        this.uri = Objects.requireNonNull(uri);
        this.host = Objects.requireNonNull(host);
        this.connection = Objects.requireNonNull(connection);
        this.accept = Objects.requireNonNull(accept);
    }

    public static HttpRequest from(InputStream in) throws IOException{
        Map<String, String> requestHeader = getRequestHeader(in);

        String method = requestHeader.get(START_LINE).split(PATH_DELIMITER)[HTTP_METHOD_POS];
        URI uri = URI.create(requestHeader.get(START_LINE).split(PATH_DELIMITER)[PATH_POS]);
        String host = requestHeader.get(HOST);
        String connection = requestHeader.get(CONNECTION);
        String accept = requestHeader.get(ACCEPT);

        return new HttpRequest(method, uri, host, connection, accept);
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

    public String getExtension() {
        String[] split = getPath().split(SLASH_DELIMITER)[EXTENSION_POS].split(DOT_DELIMITER);
        return split.length > 1 ? split[1] : split[0];
    }

    private static Map<String, String> getRequestHeader(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        Map<String, String> request = new HashMap<>();

        String line = br.readLine();
        request.put(START_LINE, line);

        while(!line.equals(END)){
            line = br.readLine();
            String[] splitLine = line.split(" ");

            if(splitLine[KEY_INDEX].equals(HOST)){
                request.put(HOST, splitLine[VALUE_INDEX]);
            }
            if(splitLine[KEY_INDEX].equals(CONNECTION)){
                request.put(CONNECTION, splitLine[VALUE_INDEX]);
            }
            if(splitLine[KEY_INDEX].equals(ACCEPT)){
                request.put(ACCEPT, splitLine[VALUE_INDEX]);
            }
        }

        return request;
    }

    @Override
    public String toString() {
        return "HttpRequest " + "{" + "\n" +
                "method='" + method + "\n" +
                "uri=" + uri + "\n" +
                "host='" + host + "\n" +
                "connection='" + connection + "\n" +
                "accept='" + accept + " }";
    }
}
