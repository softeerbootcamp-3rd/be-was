package http;

import java.util.ArrayList;
import java.util.List;

public class Cookie {
    private final String name;
    private final String value;
    private final String path;

    public Cookie(String name, String value) {
        this.name = name;
        this.value = value;
        this.path = "/";
    }

    public static Cookie from(String name, String value){
        return new Cookie(name, value);
    }

    public static List<Cookie> parse(String cookieString) {
        List<Cookie> cookies = new ArrayList<>();
        processCookieString(cookies, cookieString);
        return cookies;
    }

    private static void processCookieString(List<Cookie> cookies, String cookieString) {
        if(!"".equals(cookieString)){
            String[] cookieStringArray = cookieString.split("; ");
            for(String nowCookieString : cookieStringArray){
                String[] keyAndValue = nowCookieString.split("=");
                cookies.add(Cookie.from(keyAndValue[0], keyAndValue[1]));
            }
        }
    }

    public boolean isSid(){
        return "sid".equals(this.name);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("%s=%s; Path=%s", name, value, path);
    }
}
