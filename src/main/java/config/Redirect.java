package config;

import java.util.HashMap;

import static webserver.RequestHandler.threadUuid;

// 리디렉션을 수행해야하는 URL을 매핑
public class Redirect {
    private static HashMap<String,String> notLoggedIn = new HashMap<>();
    static {
        notLoggedIn.put("/user/list.html","/user/login.html");
        notLoggedIn.put("/qna/form.html","/user/login.html");
    }

    // 리디렉트를 해야 할 주소가 있는 경우 해당 URL을 반환, 없으면 null 반환
    public static String getNewUrl(String url){
        if(threadUuid.get() == null && notLoggedIn.containsKey(url))
            return notLoggedIn.get(url);

        return null;
    }
}
