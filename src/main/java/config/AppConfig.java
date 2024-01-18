package config;

import factory.HttpRequestFactory;
import factory.HttpRequestFactoryImpl;
import factory.HttpResponseFactory;
import factory.HttpResponseFactoryImpl;
import service.*;
import util.FileDetector;
import webApplicationServer.controller.UserController;
import webApplicationServer.controller.UserControllerImpl;
import webApplicationServer.service.UserService;
import webApplicationServer.service.UserServiceImpl;

public class AppConfig {
    public static HttpResponseSender httpResponseService() {
        return HttpResponseSenderImpl.getInstance();
    }
    public static HttpResponseFactory httpResponseFactory(){
        return HttpResponseFactoryImpl.getInstance();
    }
    public static HttpRequestFactory httpRequestFactory(){
        return HttpRequestFactoryImpl.getInstance();
    }
    public static StaticResponseBuilder staticResponseBuilder(){
        return StaticResponseBuilderImpl.getInstance();
    }
    public static UserController userController() {
        return UserControllerImpl.getInstance();
    }
    public static DynamicResponseBuilder dynamicResponseBuilder() {
        return DynamicResponseBuilderImpl.getInstance();
    }
    public static UserService userService() {
        return UserServiceImpl.getInstance();
    }
    public static FileDetector fileDetector(){return FileDetector.getInstance();}
}