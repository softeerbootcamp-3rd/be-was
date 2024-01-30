package webserver;

import config.HTTPRequest;
import config.HTTPResponse;
import controller.QnaController;
import controller.UserController;

public enum ControllerDispatcher {

    //url별로 컨트롤러에게 작업 할당
    CREATE_ACCOUNT("/user/create"){
        @Override
        public HTTPResponse toController(HTTPRequest request){
            HTTPResponse response;
            response = UserController.createAccount(request);
            return response;
        }
    },
    LOGIN("/user/login"){
        @Override
        public HTTPResponse toController(HTTPRequest request){
            HTTPResponse response;
            response = UserController.login(request);
            return response;
        }
    },
    CREATE_POST("/qna/form.html"){
        @Override
        public HTTPResponse toController(HTTPRequest request){
            HTTPResponse response;
            response = QnaController.createQna(request);
            return response;
        }
    }
    ;

    public String url;

    private ControllerDispatcher(String url){
        this.url=url;
    }

    public abstract HTTPResponse toController(HTTPRequest request);
}
