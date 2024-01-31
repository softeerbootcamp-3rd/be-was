package interceptor;

import exception.UnAuthorizedException;
import http.Cookie;
import http.Request;
import http.SessionManager;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.Model;
import webserver.dispatcherServlet;

import java.util.ArrayList;
import java.util.List;
public class Intercepter {
    private static final Logger logger = LoggerFactory.getLogger(Intercepter.class);
    private List<String> whiteList = new ArrayList<>();

    public Intercepter(){
        whiteList.add("/");
        whiteList.add("/header");
        whiteList.add("/user/login");
        whiteList.add("/user/form.html");
        whiteList.add("/user/create");
    }
    public void preHandle(Request req, Model model) {
        Cookie sid = SessionManager.findCookie(req,"SID");
        if(sid == null || SessionManager.getSession(req,"SID") == null){
            if (whiteList.contains(req.getUrl())){
                return;
            }
            throw new UnAuthorizedException("로그인 정보 없음");
        }
        else {
            User user = (User) SessionManager.getSession(req,"SID");
            model.addAttribute("user",user);
        }

    }

}
