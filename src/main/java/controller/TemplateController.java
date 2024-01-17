package controller;

import http.Request;

import java.util.Map;

public class TemplateController implements Controller
{
    @Override
    public String process(Request req, Map<String, Object> model) {
        Map<String, String> paramMap = req.getRequestParam();
        System.out.println("[TemplateController.process] template : " + paramMap.get("template"));
        return paramMap.get("template");
    }
}
