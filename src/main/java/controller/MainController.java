package controller;

import com.sun.tools.javac.Main;
import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

public class MainController {
    private String method;
    private String url;
    private HashMap<String, String> headers;
    private HashMap<String, String> params;
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public MainController(String method, String url, HashMap<String, String> headers) {
        this.method = method;
        this.url = url;
        this.headers = headers;
    }
    public MainController(String method, String url, HashMap<String, String> headers, HashMap<String, String> params) {
        this(method, url, headers);
        this.params = params;
    }
    public String getMethod() { return this.method;}
    public String getUrl() { return this.url;}
    public HashMap<String, String> getHeaders() { return this.headers;}
    public HashMap<String, String> getParams() { return this.params;}

    public byte[] control() throws IOException {
        byte[] rdata = ("알 수 없는 요청입니다!!!").getBytes();
        if(this.params == null) {
            String[] types = this.headers.get("Accept").split(",");
            String mimeType = types[0];
            if(mimeType.equals("text/html"))
                url = "./src/main/resources/templates" + url;
            else
                url = "./src/main/resources/static" + url;
            rdata = Files.readAllBytes(new File(url).toPath());
        }
        else if(this.url.equals("/user/create")) {
            String userId = this.params.get("userId");
            String password = this.params.get("password");
            String name = this.params.get("name");
            String email = this.params.get("email");
            User user = new User(userId, password, name, email);
            String result = User.verifyUser(user);
            if(result.equals("성공")) {
                Database.addUser(user);
                logger.debug("새로운 유저 생성!  " + user.toString() + "\n");
                rdata = (user.getName() + "님 안녕하세요!").getBytes();
            }
        }

        return rdata;
    }
}
