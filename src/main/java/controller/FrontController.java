package controller;

import config.AppConfig;
import controller.adapter.HandlerAdapter;
import controller.adapter.QnaControllerHandlerAdapter;
import controller.adapter.ResourceHandlerAdapter;
import controller.adapter.UserControllerHandlerAdapter;
import exception.NoHandlerAdapterFoundException;
import exception.NoHandlerFoundException;
import model.HttpRequest;
import model.HttpResponse;
import util.ResponseSender;
import util.URIParser;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrontController {
    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public FrontController() {
        initHandlerMappingMap();
        initHandlerAdapters();
    }

    private void initHandlerMappingMap() {
        handlerMappingMap.put("resource", new ResourceController());

        handlerMappingMap.put("/user/create", AppConfig.userCreateController());
        handlerMappingMap.put("/user/login", AppConfig.userLoginController());
        handlerMappingMap.put("/user/list", AppConfig.userListController());

        handlerMappingMap.put("/qna/form", AppConfig.qnaFormController());
        handlerMappingMap.put("/qna/create", AppConfig.qnaCreateController());
    }

    private void initHandlerAdapters() {
        handlerAdapters.add(new ResourceHandlerAdapter());
        handlerAdapters.add(new UserControllerHandlerAdapter());
        handlerAdapters.add(new QnaControllerHandlerAdapter());
    }

    public void service(HttpRequest httpRequest, OutputStream out) throws IOException {
        HttpResponse httpResponse = HttpResponse.newEmptyInstance();
        ModelView mv = null;
        try {
            Object handler = getHandler(httpRequest);

            HandlerAdapter adapter = getHandlerAdapter(handler);
            mv = adapter.handle(httpRequest, httpResponse, handler);
        } catch (NoHandlerFoundException | NoHandlerAdapterFoundException e) {
            e.printStackTrace();
            mv = ModelView.errorInstance();
        }

        String viewName = mv.getViewName();
        View view = ViewResolver.resolveViewName(viewName);

        DynamicPageLoader.beforeRender(httpRequest, mv);
        view.render(httpRequest, httpResponse, mv);

        ResponseSender.send(httpResponse, out);
    }

    private HandlerAdapter getHandlerAdapter(Object handler) {
        for (HandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
        throw new NoHandlerAdapterFoundException(handler + "에 해당하는 handler를 찾을 수 없습니다.");
    }

    private Object getHandler(HttpRequest httpRequest) {
        String requestURI = httpRequest.getURI();
        if (URIParser.isResourceURI(requestURI)) {
            return handlerMappingMap.get("resource");
        }

        Object handler = handlerMappingMap.get(requestURI);
        if (handler != null) {
            return handler;
        }

        throw new NoHandlerFoundException(httpRequest.getURI() + "에 해당하는 handler를 찾을 수 없습니다.");
    }
}
