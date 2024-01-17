package controller;

import annotation.GetMapping;

public class TestController {
    @GetMapping(path = "/test")
    public String test(){
        return "test";
    }
}
