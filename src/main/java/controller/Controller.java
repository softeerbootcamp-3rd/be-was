package controller;

import java.util.Map;

public interface Controller {
    String process(Map<String,String> paramMap, Map<String, Object> model);
}
