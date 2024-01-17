package model;

import java.util.HashMap;

public class Request {
    private String method;
    private String target;
    private String version;
    private String url;
    private HashMap<String, String> header = new HashMap<>();
    private HashMap<String, String> param;
    private HashMap<String, String> body;
    private String mimeType;

    public String getMethod() {return this.method;}
    public String getTarget() {return this.target;}
    public String getVersion() {return this.version;}
    public String getUrl() {return this.url;}
    public HashMap<String, String> getHeader() {return this.header;}
    public HashMap<String, String> getParam() {return this.param;}
    public HashMap<String, String> getBody() {return this.body;}
    public String getMimeType() {return this.mimeType;}

    public void parseStartLine(String line) {
        String[] tokens = line.split(" ");
        this.method = tokens[0];
        this.target = tokens[1];
        this.version = tokens[2];
        this.parseTarget();
    }
    public void putHeader(String key, String value) {
        this.header.put(key, value);
        if (key.equals("Accept"))
            parseAcceptHeader(value);
    }
    private void parseAcceptHeader(String value) {
        String[] types = this.header.get("Accept").split(",");
        this.mimeType = types[0];
    }

    @Override
    public String toString() {
        return "[method= " + method + ", target= " + target + ", version= " + version + "]";
    }

    private void parseTarget() {
        int index = target.indexOf("?");
        if(index != -1) {
            param = new HashMap<>();
            url = target.substring(0, index);
            String queryString = target.substring(index + 1);
            if(queryString.isEmpty()) return;
            this.parseQueryString(queryString);
        }
        else {
            url = target;
        }
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
