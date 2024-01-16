package controller;

import java.util.Map;

public class StaticController implements Controller{
    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {
        return paramMap.get("static");
    }
}
