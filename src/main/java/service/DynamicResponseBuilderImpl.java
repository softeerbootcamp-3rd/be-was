package service;

import config.AppConfig;
import dto.HttpResponseDto;
import model.http.request.HttpRequest;
import webApplicationServer.controller.UserController;

public class DynamicResponseBuilderImpl implements DynamicResponseBuilder {
    private static class DynamicResponseBuilderHolder{
        private static final DynamicResponseBuilder INSTANCE = new DynamicResponseBuilderImpl();
    }
    public static DynamicResponseBuilder getInstance(){
        return DynamicResponseBuilderHolder.INSTANCE;
    }
    @Override
    public void build(HttpRequest httpRequest, HttpResponseDto httpResponseDto) {
        UserController userController = AppConfig.userController();
        userController.doGet(httpRequest, httpResponseDto);
    }
}