package controller;

import dto.ResourceDto;
import exception.ExceptionHandler;
import exception.SourceException;
import model.CommonResponse;
import request.HttpRequest;
import util.ResourceHandler;
import webserver.PathHandler;
import webserver.RequestHeader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FrontController {
    private static Map<String, UserController> controllerMap = new HashMap<>();

    static {
        controllerMap.put("/user/create", new UserController());
        controllerMap.put("/index.html", new UserController());
        controllerMap.put("/user/form.html", new UserController());
        controllerMap.put("/user/login.html", new UserController());
        controllerMap.put("/user/login", new UserController());
    }

    public static CommonResponse service(HttpRequest httpRequest) throws IOException {
        // controller Mapping
        UserController controller = getController(httpRequest.getRequestHeader().getPath());
        CommonResponse response = getResponse(httpRequest.getRequestHeader(), httpRequest.getBody(),controller);
        response.setPath(response.getPath());
        return response;
    }

    private static CommonResponse getResponse(RequestHeader requestHeader, String body, UserController controller) {
        CommonResponse response = null;
        try {
            ResourceDto resource = PathHandler.responseResource(requestHeader.getMethod(), requestHeader.getPath(), body, controller);
            response = CommonResponse.onOk(resource.getHttpStatus(), ResourceHandler.resolveResource(resource),
                    resource.getExtension(), resource.getPath());
        } catch (SourceException e) {
            response = ExceptionHandler.handleGeneralException(e);
        } finally {
            return response;
        }
    }

    private static UserController getController(String path) {
        for (String key : controllerMap.keySet()) {
            if (path.startsWith(key)) {
                return controllerMap.get(key);
            }
        }
        return null;
    }
}
