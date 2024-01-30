package controller;

import constant.HttpStatus;

public class ResourceController {
    private String type;

    public ResourceController(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public ModelView process(String uri) {
        return new ModelView(uri, HttpStatus.OK);
    }
}
