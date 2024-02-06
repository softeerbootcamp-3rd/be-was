package model.http;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OptionHeaders {
    private final HashMap<String, String> optionHeaders;

    public OptionHeaders(HashMap<String, String> optionHeaders) {
        this.optionHeaders = optionHeaders;
    }

    public String getSessionIdCookie() {
        return optionHeaders.get("Cookie").split("=")[1];
    }

    public boolean hasCookie(){
        return optionHeaders.containsKey("Cookie");
    }

    public int getContentLength(){
        if(optionHeaders.containsKey("Content-Length")){
            return Integer.parseInt(optionHeaders.get("Content-Length"));
        }
        return 0;
    }
}
