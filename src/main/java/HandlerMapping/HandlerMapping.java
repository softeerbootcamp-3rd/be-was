package HandlerMapping;

import Controller.User.UserController;
import DTO.HttpStatus;
import DTO.Request;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import DTO.Response;
import Functions.FileBytes;
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

        String URI = request.GetURI();
        //body = urlParsing(URI, response);
        response = urlParsing(request, response);
        return response;



    }


    public Response urlParsing(Request request,  Response response) throws IOException {

        String URI = request.GetURI();
        String type = URI.split("/")[1];
        if(Mapping.get(type) != null){
            response = notHTML(URI, response);
        }else{
            response = HTML(URI, request);
        }

        return response;
    }

    public Response notHTML(String URI, Response response) throws IOException {
        String type = URI.split("/")[1];
        String file = URI.split("/")[2];
        response.SetreturnType(Mapping.get(type));
        //response.Setbody(Files.readAllBytes(new File(staticfilePath + "/" + type + "/" + file).toPath()));
        response.Setbody(FileBytes.FilesreadAllBytes(staticfilePath + "/" + type + "/" + file));
        response.SetHttpStatus(HttpStatus.OK);
        return response;
    }

    public Response HTML(String URI, Request request) throws IOException {

        Response response = new Response();
        byte[] body = null;
        String middleURI = URI.split("/")[1];
        if (URI.equals("/index.html")) {
            body = FileBytes.FilesreadAllBytes(filePath + "/index.html");
            response.Setbody(body);
            response.SetHttpStatus(HttpStatus.OK);
        }else if(middleURI.equals("user")){
            UserController userController = new UserController(URI.split("/")[2], request);
            response = userController.UserLogic(request);
        }
        return response;
    }


}
