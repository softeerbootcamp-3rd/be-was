package controller;

import http.Request;

import java.util.Map;

public class StaticController implements Controller{
    @Override
    public String process(Request req, Map<String, Object> model) {
        Map<String, String> paramMap = req.getRequestParam();
        return paramMap.get("static");
    }
}
