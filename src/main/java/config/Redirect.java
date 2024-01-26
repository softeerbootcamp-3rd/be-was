package config;

import java.util.HashMap;

import static webserver.RequestHandler.threadUuid;

public class Redirect {
    private static HashMap<String,String> notLoggedIn = new HashMap<>();
    static {
        notLoggedIn.put("/user/list.html","/user/login.html");
    }

    public static String getNewUrl(String url){
        if(threadUuid.get() == null && notLoggedIn.containsKey(url)) {
            return notLoggedIn.get(url);
        }
        return null;
    }
}
