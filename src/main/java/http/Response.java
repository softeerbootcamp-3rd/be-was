package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.IOException;

public class Response {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private String location;

    private HttpStatus status;
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
            dos.writeBytes("HTTP/1.1 " + status +"\r\n");
            dos.writeBytes("Content-Type: "+req.getContentType()+";charset=utf-8\r\n");
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
}
