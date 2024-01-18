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
    private String contentType = "";
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

    public void send(DataOutputStream dos, Request req){
        byte[] body = new byte[0];
        setResponseHeader(dos,body.length,req);
        setResponseBody(dos,body);
    }
    public void send(DataOutputStream dos,byte[] body, Request req){
        setResponseHeader(dos,body.length,req);
        setResponseBody(dos,body);
    }
    private void setResponseHeader(DataOutputStream dos, int lengthOfBodyContent, Request req) {
        try {
            dos.writeBytes("HTTP/1.1 " + status +"\r\n");
            if(req.getRequestParam().containsKey("content")) {
                logger.debug("content = "+req.getRequestParam().get("content"));
                setContentType(req.getRequestParam().get("content"));
                dos.writeBytes("Content-Type: "+contentType+";");
            }

            dos.writeBytes("charset=utf-8\r\n");
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
            if(fileExtension.equals("css")){
                this.contentType = "text/css";
            }
            else if(fileExtension.equals("js")){
                this.contentType = "text/javascript";
            }
            else if(fileExtension.equals("ico")){
                this.contentType = "image/x-icon";
            }
            else if(fileExtension.equals("html")){
                this.contentType = "text/html";
            }
            else if(fileExtension.equals("png")){
                this.contentType = "image/png";
            }
            else if(fileExtension.equals("jpg")){
                this.contentType = "image/jpeg";
            }
            else {
                this.contentType =  "";
                logger.warn("this.contentType = null");
            }

        }
    }
}
