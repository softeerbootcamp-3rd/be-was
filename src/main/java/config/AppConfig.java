package config;

import factory.HttpRequestFactory;
import factory.HttpRequestFactoryImpl;
import factory.HttpResponseFactory;
import factory.HttpResponseFactoryImpl;
import service.HttpResponseSender;
import service.HttpResponseSenderImpl;
import service.StaticResponseBuilder;
import service.StaticResponseBuilderImpl;

public class AppConfig {
    public HttpResponseSender httpResponseService() {
        return HttpResponseSenderImpl.getInstance();
    }
    public HttpResponseFactory httpResponseFactory(){
        return HttpResponseFactoryImpl.getInstance();
    }
    public HttpRequestFactory httpRequestFactory(){
        return HttpRequestFactoryImpl.getInstance();
    }
    public StaticResponseBuilder staticResponseBuilder(){
        return StaticResponseBuilderImpl.getInstance();
    }
}