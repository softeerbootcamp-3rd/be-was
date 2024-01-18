package http;

import jdk.jfr.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.FrontController;
import webserver.ViewResolver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Response {
    private static final Logger logger = LoggerFactory.getLogger(FrontController.class);
    private String location;
    private HttpStatus status;
    private String contentType = "";
    //constructors
    public Response(){
    }
    //getter
    public String getLocation() {
        return location;
    }
    public HttpStatus getStatus() {
        return status;
    }
    public String getContentType(){return this.contentType;}

    //setter
    public void setLocation(String location) {
        this.location = location;
    }
    public void setStatus(HttpStatus status) {
        this.status = status;
    }
    public void setContentType(String fileName){
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex != -1 && dotIndex < fileName.length() - 1) {
            String fileExtension = fileName.substring(dotIndex + 1);
            this.contentType = HttpContentType.getValue(fileExtension);
        }
    }

    //methods
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
            dos.writeBytes(getContentTypeString(req));
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

    private String getContentTypeString(Request req){
        if(req.getRequestParam().containsKey("content")) {
            logger.debug("content = "+req.getRequestParam().get("content"));
            setContentType(req.getRequestParam().get("content"));
            return "Content-Type: "+contentType+";";
        }
        return "";
    }
}
