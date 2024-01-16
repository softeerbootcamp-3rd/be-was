package controller;

import java.util.Map;

public class TemplateController implements Controller
{
    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {
        System.out.println("[TemplateController.process] template : " + paramMap.get("template"));
        return paramMap.get("template");
    }
}
