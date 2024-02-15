package controller;

import constant.HttpStatus;

public class ResourceController {
    public ResourceController() {
    }

    public ModelView process(String uri) {
        return new ModelView(uri, HttpStatus.OK);
    }
}
