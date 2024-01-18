package service;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class CreateUserService {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private String Url;


    public CreateUserService(Database db, String path) {
        try {
            String create_path = path.split("\\?")[1];
            String[] user_information = create_path.split("&");
            String[] information = new String[4];

            for (int i = 0; i < user_information.length; i++) {
                String key = user_information[i].split("=")[0];
                String value = user_information[i].split("=")[1];
                if (value!=null){

                    String decodedValue = URLDecoder.decode(value, StandardCharsets.UTF_8);
                    information[i] = decodedValue;
                }
                else{
                    throw new NullPointerException("Value is null for key: " + key);
                }

            }

            String userId = information[0];
            String password = information[1];
            String name = information[2];
            String email = information[3];

            User user = new User(userId, password, name, email);
            User userFind = db.findUserById(userId);

            if (userFind == null) {
                logger.debug("user : {}",user);
                db.addUser(user);
                this.Url = "/user/login.html";
            }
            else {
                logger.debug(">>중복된 UserId : {}",userId);
                this.Url = "/user/login_failed.html";
            }
        }
        catch (NullPointerException e) {
            // 여기서 NullPointerException 예외를 처리
            this.Url = "/user/login_failed.html";
            logger.error("Null value detected in user information", e);
        }
        catch (Exception e) {
            // 기타 예외들에 대한 처리 (NullPointerException 외의 다른 예외들)
            this.Url = "/user/login_failed.html";
            logger.error("An unexpected error occurred", e);
        }
    }
    public String getUrl() {
        return Url;
    }

}
