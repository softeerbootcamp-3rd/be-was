package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

public class Request {
    private String method;
    private String target;
    private String version;
    private String path;
    private HashMap<String, String> header = new HashMap<>();
    private HashMap<String, String> param;
    private HashMap<String, String> body;
    private String fragment;
    private String mimeType;
    private File file;

    public String getMethod() {return this.method;}
    public String getTarget() {return this.target;}
    public String getVersion() {return this.version;}
    public String getPath() {return this.path;}
    public HashMap<String, String> getHeader() {return this.header;}
    public HashMap<String, String> getParam() {return this.param;}
    public HashMap<String, String> getBody() {return this.body;}
    public String getMimeType() {return this.mimeType;}
    public File getFile() {return this.file;}

    public void parseStartLine(String line) {
        String[] tokens = line.split(" ");
        this.method = tokens[0];
        this.target = tokens[1];
        this.version = tokens[2];
        this.path = this.parseTarget(this.target);
    }
    private String parseTarget(String target) {
        target = parseFragment(target);
        target = parseParameter(target);
        parseFile(target);
        return target;
    }
    private String parseParameter(String target) {
        int indexOfParameter = target.indexOf("?");
        if(indexOfParameter != -1) {
            param = new HashMap<>();
            String queryString = target.substring(indexOfParameter + 1);
            if(!queryString.isEmpty())
                this.parseQueryString(queryString);
            target = target.substring(0, indexOfParameter);
        }
        return target;
    }
    private String parseFragment(String target) {
        int indexOfFragment = target.indexOf("#");
        if(indexOfFragment != -1) {
            this.fragment = target.substring(indexOfFragment+1);
            target = target.substring(0, indexOfFragment);
        }
        return target;
    }
    public void putHeader(String key, String value) {
        this.header.put(key, value);
        if (key.equals("Accept"))
            this.mimeType = parseAcceptHeader(value);
    }
    private String parseAcceptHeader(String value) {
        String[] types = this.header.get("Accept").split(",");
        return types[0];
    }
    private void parseFile(String target) {
        String[] tokens = target.split("/");
        if(tokens.length == 0) return;
        String lastToken = tokens[tokens.length-1];
        int indexOfFile = lastToken.lastIndexOf(".");
        if(indexOfFile == -1) return;
        this.file = new File(lastToken, lastToken.substring(indexOfFile+1));
    }

    @Override
    public String toString() {
        return "[method= " + method + ", target= " + target + ", version= " + version + "]";
    }

    private void parseQueryString(String queryString) {
        String[] keyAndValue = queryString.split("&");
        for(String keyValue : keyAndValue) {
            int indexOfEqual = keyValue.indexOf("=");
            String key = keyValue.substring(0, indexOfEqual);
            String value = keyValue.substring(indexOfEqual+1);
            param.put(key, value);
        }
    }
}
