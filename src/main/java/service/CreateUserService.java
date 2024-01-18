package service;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class CreateUserService {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private String url;


    public CreateUserService(Database db, String path) {
        try {
            String[] userInformation = path.split("\\?")[1].split("&");
            String[] information = new String[4];

            for (int i = 0; i < userInformation.length; i++) {
                String key = userInformation[i].split("=")[0];
                String value = userInformation[i].split("=")[1];
                if (value!=null){

                    String decodeValue = URLDecoder.decode(value, StandardCharsets.UTF_8);
                    information[i] = decodeValue;
                }
                else{
                    throw new NullPointerException("Value is null for key: " + key);
                }
            }
            makeUser(db,information);
        }
        catch (NullPointerException e) {
            // 여기서 NullPointerException 예외를 처리
            this.url = "/user/form_failed.html";
            logger.error("Null value detected in user information", e);
        }
        catch (Exception e) {
            // 기타 예외들에 대한 처리 (NullPointerException 외의 다른 예외들)
            this.url = "/user/form_failed.html";
            logger.error("An unexpected error occurred", e);
        }
    }
    public String getUrl() {
        return url;
    }

    public void makeUser(Database db, String[] userInformation){
        String userId = userInformation[0];
        String password = userInformation[1];
        String name = userInformation[2];
        String email = userInformation[3];

        User user = new User(userId, password, name, email);
        User userFind = db.findUserById(userId);

        if (userFind == null) {
            logger.debug("user : {}",user);
            db.addUser(user);
            this.url = "/user/login.html";
        }
        else {
            logger.debug(">>중복된 UserId : {}",userId);
            this.url = "/user/form_failed.html";
        }
    }
}
