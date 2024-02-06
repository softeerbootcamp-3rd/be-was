package controller;

import db.Database;
import httpmessage.HttpStatusCode;
import httpmessage.request.HttpRequest;
import httpmessage.response.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;
import java.io.IOException;
import java.util.Map;

public class UserCreateController implements Controller {
    private final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private String path;

    public void service(HttpRequest httpRequest, HttpResponse httpResponse){

        try {
            makeUser(httpRequest.getParmeter());
        }
        catch (NullPointerException e) {
            this.path = "/user/form_failed.html";
            logger.error("Null value detected in user information", e);
        }
        makeHttpResponse(httpResponse);
    }
    public void makeUser(Map<String,String> userInformation){
        Database db = new Database();

        String userId = userInformation.get("userId");
        String password = userInformation.get("password");
        String name = userInformation.get("name");
        String email = userInformation.get("email");

        User userFind = db.findUserById(userId);
        if (userFind == null) {
            User user = new User(userId, password, name, email);
            db.addUser(user);
            this.path = "/index.html";
        }
        else {
            logger.debug(">>중복된 UserId : {}",userId);
            this.path = "/user/form_failed.html";
        }
    }

    public void makeHttpResponse(HttpResponse httpResponse){
        httpResponse.setRedirectionPath(path);
        httpResponse.setHttpStatusCode(HttpStatusCode.MOVED_TEMPORARILY);
    }

}
