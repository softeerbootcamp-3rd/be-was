package config;

import factory.HttpRequestFactory;
import factory.HttpRequestFactoryImpl;
import factory.HttpResponseFactory;
import factory.HttpResponseFactoryImpl;
import service.*;
import util.HttpRequestParser;

public class AppConfig {
    public HttpResponseService httpResponseService() {
        return new HttpResponseServiceImpl();
    }
    public HttpResponseFactory httpResponseFactory(){
        return new HttpResponseFactoryImpl();
    }
    public HttpRequestFactory httpRequestFactory(){
        return new HttpRequestFactoryImpl(getHttpRequestParser());
    }
    public WebServerFileService webServerFileService(){
        return new WebServerFileServiceImpl();
    }
    private HttpRequestParser getHttpRequestParser() {
        return new HttpRequestParser();
    }
}