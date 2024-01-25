package controller;

import model.HttpRequest;
import model.HttpResponse;

import java.io.IOException;

import static model.HttpStatus.INTERNAL_SERVER_ERROR;

public class ResourceController {
    public static HttpResponse serveStaticFile(HttpRequest httpRequest) {
        try {
            return HttpResponse.response200(httpRequest.getExtension(), httpRequest.getPath());
        } catch (IOException e){
            return HttpResponse.errorResponse(INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
