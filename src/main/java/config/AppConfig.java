package config;

import filter.AuthFilter;
import filter.Filter;
import controller.UserControllerImpl;
import factory.HttpRequestFactory;
import factory.HttpRequestFactoryImpl;
import factory.HttpResponseFactory;
import factory.HttpResponseFactoryImpl;
import handler.*;
import service.HttpResponseSendService;
import service.HttpResponseSendServiceImpl;
import service.UserService;
import service.UserServiceImpl;
import util.FileDetector;

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

    public static StaticResponseHandler staticResponseHandler() {
        return StaticResponseHandlerImpl.getInstance();
    }

    public static UserControllerImpl userController() {
        return UserControllerImpl.getInstance();
    }

    public static DynamicResponseHandler dynamicResponseHandler() {
        return DynamicResponseHandlerImpl.getInstance();
    }

    public static UserService userService() {
        return UserServiceImpl.getInstance();
    }

    public static FileDetector fileDetector() {
        return FileDetector.getInstance();
    }

    public static Filter filter(){
        return AuthFilter.getInstance();
    }

    public static UrlControllerMapper urlControllerMapper(){
        return UrlControllerMapper.getInstance();
    }
}
