package http;

import jdk.jfr.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.FrontController;
import webserver.ViewResolver;

import java.io.DataOutputStream;
import java.io.IOException;

public class Response {
    private final ViewResolver viewResolver = new ViewResolver();
    private static final Logger logger = LoggerFactory.getLogger(FrontController.class);
    private String location;

    private HttpStatus status;
    private String contentType;
    public Response(){
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void send(DataOutputStream dos,byte[] body, Request req){
        setResponseHeader(dos,body.length,req);
        setResponseBody(dos,body);
    }
    private void setResponseHeader(DataOutputStream dos, int lengthOfBodyContent, Request req) {
        try {
            logger.debug("req.getUrl() = "+req.getUrl());
            setContentType(req.getUrl());
            dos.writeBytes("HTTP/1.1 " + status +"\r\n");
            dos.writeBytes("Content-Type: "+contentType+";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("Location : " + location + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {

            logger.error(e.getMessage());
        }
    }

    private void setResponseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
    public String getContentType(){return this.contentType;}
    public void setContentType(String url){
        String fileName = url;
        String fileExtension;
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex != -1 && dotIndex < fileName.length() - 1) {
            fileExtension = fileName.substring(dotIndex + 1);
            if(fileExtension == "css"){
                this.contentType = "text/css";
            }
            else if(fileExtension == "js"){
                this.contentType = "text/javascript";
            }
            else if(fileExtension == "ico"){
                this.contentType = "image/x-icon";
            }
            else if(fileExtension == "html"){
                this.contentType = "text/html";
            }
            else if(fileExtension == "png"){
                this.contentType = "image/png";
            }
            else if(fileExtension == "jpg"){
                this.contentType = "image/jpeg";
            }
            else {
                this.contentType =  ""; // 확장자가 없을 경우 빈 문자열 반환
            }

        }
    }
}
