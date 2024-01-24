package controller;

import config.HTTPRequest;
import config.HTTPResponse;
import config.ResponseCode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PageController {

//    static final String TEMPLATE_FILE_PATH = "/Users/qkreh/IdeaProjects/be-was/src/main/resources/templates";
//    static final String STATIC_FILE_PATH = "/Users/qkreh/IdeaProjects/be-was/src/main/resources/static";

    static final String TEMPLATE_FILE_PATH = "/Users/user/IdeaProjects/be-was/src/main/resources/templates";
    static final String STATIC_FILE_PATH = "/Users/user/IdeaProjects/be-was/src/main/resources/static";
    static public HTTPResponse getPage(HTTPRequest request) throws IOException {


        String url = request.getUrl();
        File file;

        if (url.contains(".html"))
            file = new File(TEMPLATE_FILE_PATH + url);
        else
            file = new File(STATIC_FILE_PATH + url);

        HTTPResponse response = null;



        try(FileInputStream fis = new FileInputStream(file)) {
            byte[] body = new byte[(int) file.length()];
            fis.read(body);
            byte[] head = ("HTTP/1.1" + ResponseCode.OK.code +" "+ ResponseCode.OK +" \r\n"+
                    "Content-Type: "+request.getHead().get("Accept").split(",")[0]+"\r\n"+
                    "Content-Length: " + body.length  + "\r\n").getBytes();
            response = new HTTPResponse("HTTP/1.1",ResponseCode.OK.code, ResponseCode.OK.toString(), head, body);
        }
        catch (IOException e){
            byte[] body = new byte[0];
            byte[] head = ("HTTP/1.1" + ResponseCode.NOT_FOUND.code + " " + ResponseCode.NOT_FOUND +" \r\n"+
                    "Content-Type: "+request.getHead().get("Accept").split(",")[0]+"\r\n"+
                    "Content-Length: " + body.length + "\r\n").getBytes();
            response = new HTTPResponse("HTTP/1.1",ResponseCode.NOT_FOUND.code, ResponseCode.NOT_FOUND.toString(), head, body);
        }
        catch (Exception e){
            byte[] body = new byte[0];
            byte[] head = ("HTTP/1.1" + ResponseCode.SERVER_ERROR.code + " " + ResponseCode.SERVER_ERROR +" \r\n"+
                    "Content-Type: "+request.getHead().get("Accept").split(",")[0]+"\r\n"+
                    "Content-Length: " + body.length  + "\r\n").getBytes();
            response = new HTTPResponse("HTTP/1.1",ResponseCode.SERVER_ERROR.code, ResponseCode.SERVER_ERROR.toString(), head, body);
        }

        return response;
    }




}
