package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLDecoder;
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
    private Map<String, String> requestParam = new HashMap<>();
    private Map<String, String> formData = new HashMap<>();

    public String getMethod() {
        return method;
    }
    public String getUrl() {
        return url;
    }
    public Map<String, String> getRequestParam() { return requestParam; }
    public Map<String, String> getFormData() { return formData; }


    public HttpRequest (InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        getRequestInfo(br);
    }

    private void getRequestInfo(BufferedReader br) throws IOException {

        String line = br.readLine();
        if (line != null) {
            String[] lines = line.split(" ");
            method=lines[0];
            if (lines[1].contains("?")) {
                getQueryParam(lines[1]);
                lines[1] = lines[1].substring(0,line.indexOf('?'));
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
                } else if (line.startsWith("Content-Length: ")) {
                    contentLength = Integer.parseInt(line.substring("Content-Length: ".length()));
                }
            }
            if (method.equals("POST")) {
                String body = getBody(br, contentLength);
                getFormData(body);
            }

        }
    }

    private void getQueryParam(String url) throws UnsupportedEncodingException {
        String paramLine = url.substring(url.indexOf('?')+1);
        String[] params = paramLine.split("&");
        for (String param : params) {
            String[] p = param.split("=");
            requestParam.put(p[0], decode(p[1]));
        }
    }

    private void getFormData(String body) throws UnsupportedEncodingException {
        String[] objects = body.split("&");
        for (String object : objects) {
            String[] o = object.split("=");
            formData.put(o[0], decode(o[1]));
        }
    }

    private String getBody(BufferedReader br, int contentLength) throws IOException {
        StringBuilder bodyBuilder = new StringBuilder();
        int read;
        int totalRead = 0;
        char[] buffer = new char[1024];

        // 읽을 바이트 수가 Content-Length와 일치할 때까지 읽기
        while (totalRead < contentLength && (read = br.read(buffer, 0, Math.min(buffer.length, contentLength - totalRead))) != -1) {
            bodyBuilder.append(buffer, 0, read);
            totalRead += read;
        }

        return bodyBuilder.toString();
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

    private String decode(String encodedString) throws UnsupportedEncodingException {
        return URLDecoder.decode(encodedString, "UTF-8");
    }
}
