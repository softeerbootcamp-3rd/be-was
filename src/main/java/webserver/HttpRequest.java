package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private String method;
    private String url;
    private String httpVersion;
    private String host;
    private String connection;
    private String accept;
    private Map<String, String> requestParam = new HashMap<>();

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public String getHost() {
        return host;
    }

    public String getConnection() {
        return connection;
    }

    public String getAccept() {
        return accept;
    }

    public Map<String, String> getRequestParam() { return requestParam; }


    public HttpRequest (InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

        String line = br.readLine();
        if (line != null) {
            String[] lines = line.split(" ");
            method=lines[0];
            if (lines[1].contains("?")) {
                String paramLine = lines[1].substring(lines[1].indexOf('?')+1);
                lines[1] = lines[1].substring(0,lines[1].indexOf('?'));
                String[] params = paramLine.split("&");
                for (String param : params) {
                    String[] p = param.split("=");
                    requestParam.put(p[0], p[1]);
                }
            }
            url=lines[1];
            httpVersion = lines[2];

            while (!line.equals("")) {
                line = br.readLine();
                if (line.startsWith("Accept:")) {
                    accept=line.substring("Accept: ".length());
                } else if (line.startsWith("Connection")) {
                    connection=line.substring("Connection: ".length());
                } else if (line.startsWith("Host:")) {
                    host=line.substring("Host: ".length());
                }
            }
        }
    }
    public void print() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n= = HTTP REQUEST INFORMATION = =");
        sb.append("\n"+method + " " + url + " " + httpVersion);
        sb.append("\nHost: "+host);
        sb.append("\nConection: " + connection);
        sb.append("\nAccept: " + accept+"\n");

        logger.debug(sb.toString());
    }
}
