package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

public class HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final String END = "";
    private static final String CHARSET_NAME = "UTF-8";
    private static final String HOST = "Host:";
    private static final String CONNECTION = "Connection:";
    private static final String ACCEPT = "Accept:";
    private static final String NEW_LINE = "\n";
    private static final int HTTP_METHOD_POS = 0;
    private static final int PATH_POS = 1;
    private static final int HOST_POS = 1;
    private static final int CONNECTION_POS = 2;
    private static final int ACCEPT_POS = 3;
    private static final String PATH_DELIMITER = " ";

    private final String method;
    private final URI uri;
    private final String host;
    private final String connection;
    private final String accept;

    public HttpRequest(InputStream in) throws IOException{
        String requestHeader = getRequestHeader(in);

        this.method = requestHeader.split(PATH_DELIMITER)[HTTP_METHOD_POS];
        this.uri = URI.create(requestHeader.split(PATH_DELIMITER)[PATH_POS]);
        this.host = requestHeader.split(NEW_LINE)[HOST_POS];
        this.connection = requestHeader.split(NEW_LINE)[CONNECTION_POS];
        this.accept = requestHeader.split(NEW_LINE)[ACCEPT_POS];
    }

    public URI getUri() {
        return uri;
    }

    public String getMethod() {
        return method;
    }

    private String getRequestHeader(InputStream in) throws IOException {
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
