package config;

import factory.HttpRequestFactory;
import factory.HttpRequestFactoryImpl;
import factory.HttpResponseFactory;
import factory.HttpResponseFactoryImpl;
import handler.*;
import service.HttpResponseSendService;
import service.HttpResponseSendServiceImpl;
import util.FileDetector;
import webApplicationServer.controller.UserController;
import webApplicationServer.controller.UserControllerImpl;
import webApplicationServer.service.UserService;
import webApplicationServer.service.UserServiceImpl;

public class AppConfig {
    public static HttpResponseSendService httpResponseSendService() {
        return HttpResponseSendServiceImpl.getInstance();
    }

    public static HttpResponseFactory httpResponseFactory() {
        return HttpResponseFactoryImpl.getInstance();
    }

    public static HttpRequestFactory httpRequestFactory() {
        return HttpRequestFactoryImpl.getInstance();
    }

    public static StaticResponseHandler staticResponseBuilder() {
        return StaticResponseHandlerImpl.getInstance();
    }

    public static UserController userController() {
        return UserControllerImpl.getInstance();
    }

    public static DynamicResponseHandler dynamicResponseBuilder() {
        return DynamicResponseHandlerImpl.getInstance();
    }

    public static UserService userService() {
        return UserServiceImpl.getInstance();
    }

    public static FileDetector fileDetector() {
        return FileDetector.getInstance();
    }
}