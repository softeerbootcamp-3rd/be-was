package service;

import db.Database;
import httpmessage.Request.HttpRequest;
import httpmessage.Response.HttpResponse;
import httpmessage.Response.ResponsePasing;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class UserCreateService implements Service{
    private final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private String path;

    public void process(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        String path = httpRequest.getBody().toString();
        try {
            String[] userInformation = path.split("&");
            String[] information = new String[4];

            for (int i = 0; i < userInformation.length; i++) {
                String key = userInformation[i].split("=")[0];

                if(userInformation[i].split("=").length==1)
                    throw new NullPointerException("Value is null for key: " + key);

                String value = userInformation[i].split("=")[1];

                String decodeValue = URLDecoder.decode(value, StandardCharsets.UTF_8);
                information[i] = decodeValue;

            }
            makeUser(information,httpRequest);
        }
        catch (NullPointerException e) {
            this.path = "/user/form_failed.html";
            logger.error("Null value detected in user information", e);
        }
        makeHttpReponse(httpResponse);
    }
    public void makeUser(String[] userInformation,HttpRequest httpRequest){
        Database db = new Database();

        String userId = userInformation[0];
        String password = userInformation[1];
        String name = userInformation[2];
        String email = userInformation[3];

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
