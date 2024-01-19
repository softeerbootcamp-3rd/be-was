package HandlerMapping;

import Controller.User.UserController;
import DTO.Request;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import DTO.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;


public class HandlerMapping {

    private Request request;
    private String filePath = "./src/main/resources/templates";
    private String staticfilePath = "./src/main/resources/static";

    private static Map<String, String> Mapping;

    static {
        Mapping = new HashMap<>();
        Mapping.put("css", "text/css");
        Mapping.put("js", "application/javascript");
        Mapping.put("fonts", "application/font-woff");
    }

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public HandlerMapping(Request request){
        this.request = request;
    }
    public HandlerMapping(){}

    public Response Controller() throws IOException {

        Response response = new Response();
        response.SetreturnType("text/html");
        byte[] body = null;
        String URI = request.GetURI();
        body = urlParsing(URI, response);
        response.Setbody(body);
        return response;

    }


    public byte[] urlParsing(String URI,  Response response) throws IOException {
        byte[] body = null;
        String type = URI.split("/")[1];
        if(Mapping.get(type) != null){
            body = notHTML(URI, response);
        }else{
            body = HTML(URI);
        }

        return body;
    }

    public byte[] notHTML(String URI, Response response) throws IOException {
        String type = URI.split("/")[1];
        String file = URI.split("/")[2];
        response.SetreturnType(Mapping.get(type));
        return Files.readAllBytes(new File(staticfilePath + "/" + type + "/" + file).toPath());
    }

    public byte[] HTML(String URI) throws IOException {
        byte[] body = null;
        String middleURI = URI.split("/")[1];
        if (URI.equals("/index.html")) {
            body = Files.readAllBytes(new File(filePath + "/index.html").toPath());
        }else if(middleURI.equals("user")){
            UserController userController = new UserController(URI.split("/")[2]);
            body = userController.UserLogic();
        }
        return body;
    }


}
