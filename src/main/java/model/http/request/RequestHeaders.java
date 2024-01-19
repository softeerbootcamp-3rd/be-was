package model.http.request;

import java.util.HashMap;

public class RequestHeaders {
    private final String host;
    private final String userAgent;
    private final String accept;
    private final HashMap<String, String> etc;

    public RequestHeaders(String host, String userAgent, String accept, HashMap<String, String> etc) {
        this.host = host;
        this.userAgent = userAgent;
        this.accept = accept;
        this.etc = etc;
    }

    public String getHost() {
        return host;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getAccept() {
        return accept;
    }

    public HashMap<String, String> getEtc() {
        return etc;
    }

    @Override
    public String toString() {
        return "{" +
                "host='" + host + '\n' +
                ", userAgent='" + userAgent + '\n' +
                ", accept='" + accept + '\n' +
                ", etc=" + etc +
                '}' + "\n";
    }
}