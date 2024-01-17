package config;

import factory.HttpRequestFactory;
import factory.HttpRequestFactoryImpl;
import factory.HttpResponseFactory;
import factory.HttpResponseFactoryImpl;
import service.*;
import webApplicationServer.controller.UserController;
import webApplicationServer.controller.UserControllerImpl;

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
        return new DynamicResponseBuilderImpl();
    }
}