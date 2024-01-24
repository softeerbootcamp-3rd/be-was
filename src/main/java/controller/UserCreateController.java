package controller;

import db.Database;
import httpmessage.request.HttpRequest;
import httpmessage.response.HttpResponse;
import httpmessage.response.ResponsePasing;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;
import java.io.IOException;
import java.util.Map;

public class UserCreateController implements Controller {
    private final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private String path;

    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {

        try {

            makeUser(httpRequest.getParmeter(),httpRequest);
        }
        catch (NullPointerException e) {
            this.path = "/user/form_failed.html";
            logger.error("Null value detected in user information", e);
        }
        makeHttpReponse(httpResponse);
    }
    public void makeUser(Map<String,String> userInformation, HttpRequest httpRequest){
        Database db = new Database();

        String userId = userInformation.get("userId");
        String password = userInformation.get("password");
        String name = userInformation.get("name");
        String email = userInformation.get("email");

        User user = new User(userId, password, name, email);
        User userFind = db.findUserById(userId);

        if (userFind == null) {
            logger.debug("user : {}",user);
            db.addUser(user);
            this.path = "/index.html";
        }
        else {
            logger.debug(">>중복된 UserId : {}",userId);
            this.path = "/user/form_failed.html";
        }
    }

    public void makeHttpReponse(HttpResponse httpResponse) throws IOException {

        ResponsePasing responsePasing = new ResponsePasing();
        int statusCode = 302;
        httpResponse.setPath(this.path);
        httpResponse.setStatusCode(statusCode);

    }

}
