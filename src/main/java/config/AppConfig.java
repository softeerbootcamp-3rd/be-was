package config;

import factory.HttpRequestFactory;
import factory.HttpRequestFactoryImpl;
import factory.HttpResponseFactory;
import factory.HttpResponseFactoryImpl;
import service.*;
import util.HttpRequestParser;

public class AppConfig {
    public HttpResponseService httpResponseService() {
        return HttpResponseServiceImpl.getInstance();
    }
    public HttpResponseFactory httpResponseFactory(){
        return HttpResponseFactoryImpl.getInstance();
    }
    public HttpRequestFactory httpRequestFactory(){
        return HttpRequestFactoryImpl.getInstance();
    }
    public WebServerFileService webServerFileService(){
        return WebServerFileServiceImpl.getInstance();
    }
}