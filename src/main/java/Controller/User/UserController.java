package Controller.User;

import db.Database;
import model.User;

import javax.xml.crypto.Data;
import java.io.File;
import java.nio.file.Files;

public class UserController {

    private String URI;


    private byte[] body;
    public UserController(String URI){
        this.URI = URI;
    }

    private String userFile = "./src/main/resources/templates/user";
    private String filePath = "./src/main/resources/templates";
    public byte[] UserLogic(){

        try {
            if ("form.html".equals(URI)) {
                body = Files.readAllBytes(new File(filePath + "/user/form.html").toPath());
            }else if("create".equals(URI.substring(0,6))){

                String []args = URI.split("&");
                String userId = args[0].split("\\?")[1].split("=")[1];
                String password = args[1].split("=")[1];
                String name = args[2].split("=")[1];
                String email = args[3].split("=")[1];
                User user = new User(userId, password, name, email);

                User finduser = Database.findUserById(userId);
                if(finduser == null){
                    Database.addUser(user);
                    body = Files.readAllBytes(new File(userFile + "/login.html").toPath());

                }else{
                    body = Files.readAllBytes(new File(userFile + "/signup_failed.html").toPath());
                }

            }else if("login.html".equals(URI)){
                body = Files.readAllBytes(new File(filePath + "/user/login.html").toPath());
            }
        }catch(Exception e){

        }
        return body;
    }

}
