package webserver;

import config.HTTPRequest;
import config.HTTPResponse;
import controller.PageController;
import controller.UserController;

public enum ControllerHandler {

    CREATE_ACCOUNT("/user/create"){
        @Override
        public HTTPResponse toController(HTTPRequest request){
            HTTPResponse response;
            response = UserController.createAccount(request);
            return response;
        }
    }

    ;

    public String url;

    private ControllerHandler(String url){
        this.url=url;
    }

    public abstract HTTPResponse toController(HTTPRequest request);
}
