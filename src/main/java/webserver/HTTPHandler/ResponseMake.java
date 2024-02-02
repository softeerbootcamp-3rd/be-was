package webserver.HTTPHandler;

import HTTPModel.HttpStatus;
import HTTPModel.Request;
import HTTPModel.Response;

import java.io.DataOutputStream;
import java.io.IOException;

public class ResponseMake {

    public ResponseMake(){};

    public static void responseRedirectWithoutBody(DataOutputStream dos, Request request, Response response) throws IOException {

        dos.writeBytes(request.GetVersion() + " " + response.getStatus().getStatusCode() + " " + response.getStatus().getMessage() + "\r\n");
        if(response.getSidSet()){
            dos.writeBytes("Set-Cookie: " + "sid="+response.getSid()+"; "+"Path=/\r\n");
        }
        dos.writeBytes("Location: " + response.getRedirectUrl() + "\r\n");
        dos.writeBytes("\r\n");
        dos.flush();
    }

    public static void responseOKWithBody(DataOutputStream dos, Request request, Response response) throws IOException {
        String contentType = response.getReturnType();

        dos.writeBytes(request.GetVersion() + " " + response.getStatus().getStatusCode() + " " + response.getStatus().getMessage() + "\r\n");
        dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + response.getBody().length + "\r\n");
        dos.writeBytes("\r\n");
        dos.write(response.getBody(), 0, response.getBody().length);
        dos.flush();
    }

    public static void responseWithoutBody(DataOutputStream dos, Request request, Response response) throws IOException {
        dos.writeBytes(request.GetVersion() + " " + response.getStatus().getStatusCode() + " " + response.getStatus().getMessage() + "\r\n");
        dos.writeBytes("\r\n");
        dos.flush();
    }

    public static void sendResponse(Request request, Response response, DataOutputStream dos) throws IOException {
        if (response.getStatus() == HttpStatus.REDIRECT) {
            responseRedirectWithoutBody(dos, request, response);
            return;
        }
        if (response.getStatus() == HttpStatus.OK) {
            responseOKWithBody(dos, request, response);
            return;
        }
        responseWithoutBody(dos, request, response);
    }

}
