package utils;

import db.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.Request;

public class LoginChecker {
    private static final Logger logger = LoggerFactory.getLogger(LoginChecker.class);
    public static boolean loginCheck(Request request){

        //Cookie값이 존재하지 않을때
        if(request.getRequestHeader().get("Cookie")==null){
            logger.debug("NO Cookie");
            return false;
        }
        String sessionVal = request.getRequestHeader().get("Cookie").split("=")[1];
        //Session값이 존재하지 않을때
        if(SessionManager.findUserById(sessionVal)==null){
            logger.debug("NO Session");
            return false;
        }
        return true;
    }
}
