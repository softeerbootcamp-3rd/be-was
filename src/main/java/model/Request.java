package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Util;
import webserver.WebServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Request {
    private static final Logger logger = LoggerFactory.getLogger(Request.class);
    private static final HashSet<String> printedKey =  // logger.debug로 출력할 헤더값들
            new HashSet<>(Arrays.asList("accept", "cookie")); // etc. User-Agent, Host
    private String method, path, version;
    private HashMap<String, String> header, param, body;
    private String fragment, mimeType, sessionId;
    private HashMap<String, String> cookie;

    public String getMethod() {return this.method;}
    public String getVersion() {return this.version;}
    public String getPath() {return this.path;}
    public HashMap<String, String> getHeader() {return this.header;}
    public HashMap<String, String> getParam() {return this.param;}
    public HashMap<String, String> getBody() {return this.body;}
    public String getMimeType() {return this.mimeType;}
    public String getSessionId() {return this.sessionId;}
    public HashMap<String, String> getCookie() {return this.cookie;}

    public Request(BufferedReader br) throws IOException {
        readRequestStartLine(br);
        readRequestHeader(br);
        readRequestBody(br);
    }
    private void readRequestStartLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        parseStartLine(line);
        logger.debug(line);
    }
    private void readRequestHeader(BufferedReader br) throws IOException {
        String line;
        this.header = new HashMap<>();
        while(true) {
            line = br.readLine();
            if(line == null || line.isEmpty()) break;
            int indexOfDelimiter = line.indexOf(":");
            String key = line.substring(0, indexOfDelimiter).trim().toLowerCase();
            String value = line.substring(indexOfDelimiter+1).trim();
            this.header.put(key, value);
            if(printedKey.contains(key))
                logger.debug(line);
        }
        setMimeType();
        parseCookie();
    }
    private void readRequestBody(BufferedReader br) throws IOException {
        int contentLength = Integer.parseInt(header.getOrDefault("content-length", "0"));
        if(contentLength == 0) return;
        this.body = new HashMap<>();
        String contentType = header.get("content-type");
        char[] body = new char[contentLength];
        br.read(body);

        if("application/x-www-form-urlencoded".equals(contentType)) {
            HashMap<String, String> hashMap = Util.parseQueryString(new String(body));
            this.body = hashMap;
        }
        else if("application/json".equals(contentType)) {
            HashMap<String, String> hashMap = Util.parseStringJson(new String(body));
            this.body = hashMap;
        }
    }
    private void setMimeType() {
        String accept = this.header.get("accept");
        if(accept == null) return;
        String[] types = accept.split(",");
        this.mimeType = types[0].trim();
    }
    public void parseStartLine(String line) {
        String[] tokens = line.split(" ");
        this.method = tokens[0];
        this.path = tokens[1];
        this.version = tokens[2];
        parsePath();
    }
    private void parsePath() {
        parseFragment();
        parseParameter();
    }
    private void parseFragment() {
        int indexOfDelimiter = path.indexOf("#");
        if(indexOfDelimiter == -1) return;
        this.fragment = path.substring(indexOfDelimiter+1);
        path = path.substring(0, indexOfDelimiter);
    }
    private void parseParameter() {
        int indexOfDelimiter = path.indexOf("?");
        if(indexOfDelimiter == -1) return;
        param = new HashMap<>();
        String queryString = path.substring(indexOfDelimiter + 1);
        if(!queryString.isEmpty())
            this.param = Util.parseQueryString(queryString);
        path = path.substring(0, indexOfDelimiter);
    }
    public void parseCookie() {
        String cookies = this.header.get("cookie");
        if(cookies == null) return;
        this.cookie = Util.parseSemicolon(cookies);
        this.sessionId = this.cookie.get("sessionId");
    }
    @Override
    public String toString() {
        return "[method= " + method + ", target= " + path + ", version= " + version + "]";
    }
}