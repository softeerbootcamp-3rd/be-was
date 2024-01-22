package controller;

import dto.ResourceDto;
import exception.SourceException;
import model.CommonResponse;
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
    }

    public static CommonResponse service(RequestHeader requestHeader) throws IOException {
        // controller Mapping
        UserController controller = getController(requestHeader.getPath());
        CommonResponse response = getResponse(requestHeader, controller);
        return response;
    }

    private static CommonResponse getResponse(RequestHeader requestHeader, UserController controller) {
        CommonResponse response = null;
        try {
            ResourceDto resource = PathHandler.responseResource(requestHeader.getMethod(), requestHeader.getPath(), controller);
            response = CommonResponse.onOk(resource.getHttpStatus(), ResourceHandler.resolveResource(resource), resource.getExtension());
        } catch (SourceException e) {
            response = CommonResponse.onFail(e.getErrorCode().getHttpStatus(), e.getMessage());
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
