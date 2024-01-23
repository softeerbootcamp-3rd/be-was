package controller;

import model.HttpRequest;
import model.HttpResponse;

import java.io.IOException;

import static webserver.HttpStatus.INTERNAL_SERVER_ERROR;
import static webserver.HttpStatus.OK;

public class ResourceController {
    public static HttpResponse serveStaticFile(HttpRequest httpRequest) {
        try {
            return HttpResponse.response200(OK, httpRequest.getExtension(), httpRequest.getPath());
        } catch (IOException e){
            return HttpResponse.errorResponse(INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
