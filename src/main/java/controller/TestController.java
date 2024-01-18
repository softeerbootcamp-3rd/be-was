package controller;

import webserver.annotation.GetMapping;

public class TestController {
    @GetMapping(path = "/test")
    public String test(){
        return "test";
    }
}
