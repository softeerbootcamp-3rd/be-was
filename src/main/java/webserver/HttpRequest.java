package webserver;

import util.URIParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class HttpRequest {

    private final String method;
    private final String URI;
    private final Map<String, String> paramMap;
    private String host;
    private String connection;
    private String origin;
    private String userAgent;
    private String accept;
    private String acceptLanguage;


    public HttpRequest(String requestString) {
        String[] requestParts = requestString.split(" ");
        this.method = requestParts[0];
        this.URI = URIParser.extractPath(requestParts[1]);
        this.paramMap = URIParser.parseQueryString(URIParser.extractQuery(requestParts[1]));
    }

    public HttpRequest(BufferedReader reader) throws IOException {
        String requestLine = reader.readLine();
        String[] requestParts = requestLine.split(" ");
        this.method = requestParts[0];
        this.URI = URIParser.extractPath(requestParts[1]);
        this.paramMap = URIParser.parseQueryString(URIParser.extractQuery(requestParts[1]));

        String s;
        while (!(s = reader.readLine()).isEmpty()) {
            requestParts = s.split(":\\s*", 2);
            if (requestParts.length == 2) {
                switch(requestParts[0]) {
                    case "Host":
                        this.host = requestParts[1];
                        break;
                    case "Connection":
                        this.connection = requestParts[1];
                        break;
                    case "Origin":
                        this.origin = requestParts[1];
                        break;
                    case "User-Agent":
                        this.userAgent = requestParts[1];
                        break;
                    case "Accept":
                        this.accept = requestParts[1];
                        break;
                    case "Accept-Language":
                        this.acceptLanguage = requestParts[1];
                        break;
                }
            }
        }
    }

    public String getMethod() {
        return method;
    }

    public String getURI() {
        return URI;
    }

    public String getHost() {
        return host;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public String getConnection() {
        return connection;
    }

    public String getOrigin() {
        return origin;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getAccept() {
        return accept;
    }

    public String getAcceptLanguage() {
        return acceptLanguage;
    }
}
