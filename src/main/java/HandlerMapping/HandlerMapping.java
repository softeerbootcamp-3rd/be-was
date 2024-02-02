package HandlerMapping;

import Controller.User.QnaController;
import Controller.User.UserController;
import HTTPModel.HttpStatus;
import HTTPModel.Request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import HTTPModel.Response;
import Functions.FileBytes;
import HTTPModel.Session;
import model.User;
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
        Mapping.put("images", "image/png");
    }

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public HandlerMapping(Request request){
        this.request = request;
    }
    public HandlerMapping(){}

    public Response Controller() throws IOException {

        Response response = new Response();
        String URI = request.GetURI();
        response = divideContents(request, response);
        return response;

    }



    public Response divideContents(Request request,  Response response) throws IOException {

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
        response.Setbody(FileBytes.FilesreadAllBytes(staticfilePath + "/" + type + "/" + file, null,false));
        response.SetHttpStatus(HttpStatus.OK);
        return response;
    }

    public Response HTML(String URI, Request request) throws IOException {

        Response response = new Response();
        byte[] body = null;

        String sid = request.GetSid();
        User logined_user = null;
        if(sid != null ) {
            logined_user = Session.SessionMap.get(sid);
        }


        String middleURI = URI.split("/")[1];

        if (URI.equals("/index.html")) {

            body = FileBytes.FilesreadAllBytes(filePath + "/index.html", logined_user, true);
            response.Setbody(body);
            response.SetHttpStatus(HttpStatus.OK);

        }else {


            if(middleURI.equals("user")){
                UserController userController = new UserController(URI.split("/")[2], request);
                response = userController.UserLogic(request);
            }else if(middleURI.equals("qna")){
                QnaController qnaController = new QnaController(URI.split("/")[2], request);
                response = qnaController.QnaLogic(request);
            }
        }
        response.SetreturnType("text/html; charset=UTF-8");
        return response;
    }


}
