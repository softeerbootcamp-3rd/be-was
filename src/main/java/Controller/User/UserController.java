package Controller.User;

import DTO.HttpStatus;
import DTO.Request;
import DTO.Response;
import Functions.FileBytes;
import com.sun.net.httpserver.HttpServer;
import db.Database;
import model.User;

import javax.xml.crypto.Data;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class UserController {

    private String URI;

    private Request request;

    private byte[] body;

    private static Map<String, User> Session = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(webserver.RequestHandler.class);
    public UserController(String URI, Request request)
    {
        this.URI = URI;
        this.request = request;
    }

    private String userFile = "./src/main/resources/templates/user";
    private String filePath = "./src/main/resources/templates";



    public Response UserLogic(Request request) {

        Response response = new Response();
        String sid = request.GetSid();
        try {
            if ("form.html".equals(URI)) {
                body = FileBytes.FilesreadAllBytes(filePath + "/user/form.html");
                response.SetHttpStatus(HttpStatus.OK);

            }else if("login".equals(URI)){
                String body_str = request.GetBody();

                String userId = body_str.split("&")[0].split("=")[1];
                String password = body_str.split("&")[1].split("=")[1];
                User user = Session.get(sid);


                if(user!= null && password.equals(user.getPassword()) && userId.equals(user.getUserId())){ // 로그인 성공
                    body = FileBytes.FilesreadAllBytes(filePath + "/index.html");
                    response.SetSid();
                    response.SetSidSet();
                    response.SetRedirectUrl(HttpStatus.REDIRECT, "/index.html");

                    logger.debug("login SUCCEESSSS: ");


                }else{
                    body = FileBytes.FilesreadAllBytes(userFile + "/login_failed.html");
                    response.SetHttpStatus(HttpStatus.OK);
                }

            }else if(URI.startsWith("create")){

                String body_str = request.GetBody();

                logger.debug(body_str);
                String userId = body_str.split("&")[0].split("=")[1];
                String password = body_str.split("&")[1].split("=")[1];
                String name = body_str.split("&")[2].split("=")[1];
                String email = body_str.split("&")[3].split("=")[1];
                User finduser = Database.findUserById(userId);
                if(finduser == null){
                    User user = new User(userId, password, name, email);
                    Database.addUser(user);

                    Session.put(sid,user);

                    body = FileBytes.FilesreadAllBytes(filePath + "/index.html");

                    response.SetRedirectUrl(HttpStatus.REDIRECT, "/index.html");

                }else{

                    body = FileBytes.FilesreadAllBytes(userFile + "/signup_failed.html");
                    response.SetHttpStatus(HttpStatus.OK);
                }



            }else if("login.html".equals(URI)){
                body = FileBytes.FilesreadAllBytes(filePath + "/user/login.html");
                response.SetHttpStatus(HttpStatus.OK);

            }
        }catch(Exception e){

        }
        response.Setbody(body);
        return response;
    }

}
