package config;

import java.util.HashMap;

import static webserver.RequestHandler.threadUuid;

// 리디렉션을 수행해야하는 URL을 매핑
public class Redirect {
    private static HashMap<String,String> notLoggedIn = new HashMap<>();
    static {
        notLoggedIn.put("/user/list.html","/user/login.html");
        notLoggedIn.put("/qna/form.html","/user/login.html");
        notLoggedIn.put("/qna/show.html","/user/login.html");
    }

    // 리디렉트를 해야 할 주소가 있는 경우 해당 URL을 반환, 없으면 null 반환
    public static String getNewUrl(String url){
        //로그인 안된 유저: URL중 notLoggedIn의 키값을 포함한 경우 리디렉션
        if(threadUuid.get() == null){
            for(String key: notLoggedIn.keySet()){
                if(url.contains(key))
                    return notLoggedIn.get(key);
            }
        }

        return null;
    }
}
