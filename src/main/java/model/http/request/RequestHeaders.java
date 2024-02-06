package model.http.request;

import model.http.OptionHeaders;

import java.util.HashMap;

public class RequestHeaders {
    private final String host;
    private final String userAgent;
    private final String accept;
    private final OptionHeaders optionHeaders;

    public RequestHeaders(String host, String userAgent, String accept, HashMap<String, String> optionHeaders) {
        this.host = host;
        this.userAgent = userAgent;
        this.accept = accept;
        this.optionHeaders = new OptionHeaders(optionHeaders);
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
    public String getUserSessionId() {
        return optionHeaders.getSessionIdCookie();
    }
    public boolean hasCookie(){
        return optionHeaders.hasCookie();
    }
    public int getContentLength(){
        return optionHeaders.getContentLength();
    }
    @Override
    public String toString() {
        return "{" +
                "host='" + host + '\n' +
                ", userAgent='" + userAgent + '\n' +
                ", accept='" + accept + '\n' +
                ", optionHeaders=" + optionHeaders +
                '}' + "\n";
    }
}
