package webserver;

import config.HTTPRequest;
import config.HTTPResponse;
import controller.PageController;
import controller.UserController;

public enum ControllerHandler {

//    INDEX("/index.html"){
//        @Override
//        public HTTPResponse toController(HTTPRequest request){
//            HTTPResponse response;
//            //컨트롤러 배정후 리스폰스 값 리턴
//            response = PageController.getPage(request);
//            return response;
//        }
//    },
//    USER_FORM("/user/form.html"){
//        @Override
//        public HTTPResponse toController(HTTPRequest request){
//            HTTPResponse response;
//            response = PageController.getPage(request);
//            return response;
//        }
//    },
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
