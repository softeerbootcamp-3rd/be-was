package controller;

import webserver.annotation.GetMapping;
import webserver.response.Response;
import webserver.type.ContentType;

public class TestController {
    @GetMapping(path = "/test")
    public Response test(){
        return Response.onSuccess(ContentType.HTML, "test".getBytes());
    }
}
