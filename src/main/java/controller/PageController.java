package controller;

import config.HTTPRequest;
import config.HTTPResponse;
import config.ResponseCode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class PageController {

    static final String TEMPLATE_FILE_PATH = "/Users/qkreh/IdeaProjects/be-was/src/main/resources/templates";
    static final String STATIC_FILE_PATH = "/Users/qkreh/IdeaProjects/be-was/src/main/resources/static";

    //static final String TEMPLATE_FILE_PATH = "/Users/user/IdeaProjects/be-was/src/main/resources/templates";
    //static final String STATIC_FILE_PATH = "/Users/user/IdeaProjects/be-was/src/main/resources/static";
    static public HTTPResponse getPage(HTTPRequest request){

        String url = request.getUrl();
        Path path;

        if(url.contains(".html"))
            path = new File(TEMPLATE_FILE_PATH + url).toPath();
        else
            path = new File(STATIC_FILE_PATH + url).toPath();

        HTTPResponse response = null;

        try {

            byte[] body = Files.readAllBytes(path);
            byte[] head = ("HTTP/1.1" + ResponseCode.OK.code + ResponseCode.OK +" \r\n"+
                    "Content-Type: "+request.getHead().get("Accept").split(",")[0]+"\r\n"+
                    "Content-Length: " + body.length  + "\r\n").getBytes();
            response = new HTTPResponse("HTTP/1.1",ResponseCode.OK.code, ResponseCode.OK.toString(), head, body);
        }
        catch (IOException e){
            byte[] body = new byte[0];
            byte[] head = ("HTTP/1.1" + ResponseCode.NOT_FOUND.code + ResponseCode.NOT_FOUND +" \r\n"+
                    "Content-Type: "+request.getHead().get("Accept").split(",")[0]+"\r\n"+
                    "Content-Length: " + body.length + "\r\n").getBytes();
            response = new HTTPResponse("HTTP/1.1",ResponseCode.NOT_FOUND.code, ResponseCode.NOT_FOUND.toString(), head, body);
        }
        catch (Exception e){
            byte[] body = new byte[0];
            byte[] head = ("HTTP/1.1" + ResponseCode.SERVER_ERROR.code + ResponseCode.SERVER_ERROR +" \r\n"+
                    "Content-Type: "+request.getHead().get("Accept").split(",")[0]+"\r\n"+
                    "Content-Length: " + body.length  + "\r\n").getBytes();
            response = new HTTPResponse("HTTP/1.1",ResponseCode.SERVER_ERROR.code, ResponseCode.SERVER_ERROR.toString(), head, body);
        }

        return response;
    }




}
