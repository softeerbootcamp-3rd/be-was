package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.*;
import java.net.URLDecoder;
import java.util.Arrays;
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
    private Integer contentLength;
    private String sessionId;
    private String userId;
    private final Map<String, String> requestParam;
    private final Map<String, String> formData;

    public String getMethod() {
        return method;
    }
    public String getUrl() {
        return url;
    }
    public String getSessionId() {
        if(sessionId==null)
            return null;
        return sessionId;
    }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserId() { return userId; }
    public Map<String, String> getRequestParam() { return requestParam; }
    public Map<String, String> getFormData() { return formData; }

    public HttpRequest(InputStream inputStream) throws IOException {
        requestParam = new HashMap<>();
        formData = new HashMap<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        getRequestInfo(br);
    }

    private void getRequestInfo(BufferedReader br) throws IOException {
        String line = br.readLine();
        parseRequestLine(line);
        parseRequestHeaders(br);
        if (method.equals("POST")) {
            getFormData(getBody(br, contentLength));
        }
    }

    private void parseRequestLine(String line) throws UnsupportedEncodingException {
        String[] lines = line.split(" ");
        method = lines[0];
        if (lines[1].contains("?")) {
            getQueryParam(lines[1]);
            lines[1] = lines[1].substring(0, lines[1].indexOf('?'));
        }
        url = lines[1];
        httpVersion = lines[2];
    }

    private void parseRequestHeaders(BufferedReader br) throws IOException {
        String line = br.readLine();
        while (!line.equals("")) {
            if (line.startsWith("Accept:")) {
                accept = line.substring("Accept: ".length());
            } else if (line.startsWith("Connection")) {
                connection = line.substring("Connection: ".length());
            } else if (line.startsWith("Host:")) {
                host = line.substring("Host: ".length());
            } else if (line.startsWith("Content-Length: ")) {
                contentLength = Integer.parseInt(line.substring("Content-Length: ".length()));
            } else if (line.startsWith("Cookie:")) {
                line = line.substring("Cookie: ".length());
                int startIndex = line.indexOf("sid=");
                if (startIndex != -1) {
                    line = line.substring(startIndex + 4);
                }
                sessionId = line;
            }
            line = br.readLine();
        }
    }

    private void getQueryParam(String url) throws UnsupportedEncodingException {
        String paramLine = url.substring(url.indexOf('?') + 1);
        Arrays.stream(paramLine.split("&"))
                .map(param -> param.split("="))
                .forEach(p -> {
                    try {
                        requestParam.put(p[0], decode(p[1]));
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                });

    }

    private void getFormData(String body) throws UnsupportedEncodingException {
        Arrays.stream(body.split("&"))
                .map(object -> object.split("="))
                .forEach(o -> {
                    try {
                        formData.put(o[0], decode(o[1]));
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                });

    }

    private String getBody(BufferedReader br, int contentLength) throws IOException {
        StringBuilder bodyBuilder = new StringBuilder();
        int read;
        int totalRead = 0;
        char[] buffer = new char[contentLength];

        // 읽을 바이트 수가 Content-Length와 일치할 때까지 읽기
        while (totalRead < contentLength && (read = br.read(buffer, 0, Math.min(buffer.length, contentLength - totalRead))) != -1) {
            bodyBuilder.append(buffer, 0, read);
            totalRead += read;
        }

        return bodyBuilder.toString();
    }

    private String decode(String encodedString) throws UnsupportedEncodingException {
        return URLDecoder.decode(encodedString, "UTF-8");
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
