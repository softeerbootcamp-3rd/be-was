package controller;

import webserver.annotation.GetMapping;
import webserver.annotation.PostMapping;
import webserver.response.Response;
import webserver.type.ContentType;

public class TestController {
    @GetMapping(path = "/test")
    public Response test(){
        return Response.onSuccess("test".getBytes());
    }

    @PostMapping(path = "/test")
    public Response postTest(){
        return Response.onSuccess("post test".getBytes());
    }
}
