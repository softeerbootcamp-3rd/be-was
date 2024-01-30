package controller;

import constant.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class ModelView {
    private String viewName;
    private Map<String, Object> model = new HashMap<>();
    private HttpStatus httpStatus;

    public ModelView(String viewName, HttpStatus httpStatus) {
        this.viewName = viewName;
        this.httpStatus = httpStatus;
    }

    public ModelView(String viewName, Map<String, Object> model, HttpStatus httpStatus) {
        this.viewName = viewName;
        this.model = model;
        this.httpStatus = httpStatus;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }

    public void addAttribute(String key, Object value) {
        model.put(key, value);
    }

    public Object getAttribute(String key) {
        return model.get(key);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
