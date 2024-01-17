package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Objects;

import static constant.HttpRequestConstant.*;

public class HttpRequest {
    private final String method;
    private final URI uri;
    private final String host;
    private final String connection;
    private final String accept;

    public HttpRequest(String method, URI uri, String host, String connection, String accept) {
        this.method = Objects.requireNonNull(method);
        this.uri = Objects.requireNonNull(uri);
        this.host = Objects.requireNonNull(host);
        this.connection = Objects.requireNonNull(connection);
        this.accept = Objects.requireNonNull(accept);
    }

    public static HttpRequest from(InputStream in) throws IOException{
        String requestHeader = getRequestHeader(in);

        String method = requestHeader.split(PATH_DELIMITER)[HTTP_METHOD_POS];
        URI uri = URI.create(requestHeader.split(PATH_DELIMITER)[PATH_POS]);
        String host = requestHeader.split(NEW_LINE)[HOST_POS];
        String connection = requestHeader.split(NEW_LINE)[CONNECTION_POS];
        String accept = requestHeader.split(NEW_LINE)[ACCEPT_POS];

        return new HttpRequest(method, uri, host, connection, accept);
    }

    public URI getUri() {
        return uri;
    }

    public String getMethod() {
        return method;
    }

    private static String getRequestHeader(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, CHARSET_NAME));
        StringBuilder request = new StringBuilder();

        String line = br.readLine();
        request.append(line).append(NEW_LINE);

        while(!line.equals(END)){
            line = br.readLine();
            if(line.startsWith(HOST) || line.startsWith(CONNECTION) || line.startsWith(ACCEPT)){
                request.append(line).append(NEW_LINE);
            }
        }

        logger.debug(request.toString());
        return request.toString();
    }

}
