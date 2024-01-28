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

        //파일 불러오기
        String url = request.getUrl();
        File file;

        if (url.contains(".html"))
            file = new File(TEMPLATE_FILE_PATH + url);
        else
            file = new File(STATIC_FILE_PATH + url);

        HTTPResponse response = null;

        //파일을 불러온 후 response에 저장, 성공시 OK, 못 찾을 시 NOT_FOUND, 그 외엔 SERVER_ERROR
        try(FileInputStream fis = new FileInputStream(file)) {
            byte[] body = new byte[(int) file.length()];
            fis.read(body);
            byte[] head = ("HTTP/1.1" + ResponseCode.OK.code +" "+ ResponseCode.OK +" \r\n"+
                    "Content-Type: "+ MIMEType(url)+"\r\n"+
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
    //MIME 타입 구하기 (여유되면 ENUM으로 뺴기)
    private static String MIMEType(String url){
        String type = "text/plain";
        if(url.contains(".html"))
            type = "text/html";
        else if(url.contains(".css"))
            type = "text/css";
        else if(url.contains(".js"))
            type = "application/javascript";
        else if(url.contains(".ico"))
            type = "image/x-icon";
        else if(url.contains(".png"))
            type = "image/png";
        else if(url.contains(".jpg"))
            type = "image/jpeg";
        return type;

    }






}
