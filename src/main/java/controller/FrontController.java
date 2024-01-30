package controller;

import config.AppConfig;
import controller.adapter.HandlerAdapter;
import controller.adapter.QnaControllerHandlerAdapter;
import controller.adapter.ResourceHandlerAdapter;
import controller.adapter.UserControllerHandlerAdapter;
import model.HttpRequest;
import model.HttpResponse;
import util.ResponseSender;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public class FrontController {
    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public FrontController() {
        initHandlerMappingMap();
        initHandlerAdapters();
    }

    private void initHandlerMappingMap() {
        // todo 제네릭으로 만들까?
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
        Object handler = getHandler(httpRequest);
        HttpResponse httpResponse = HttpResponse.newEmptyInstance();

        HandlerAdapter adapter = getHandlerAdapter(handler);
        ModelView mv = adapter.handle(httpRequest, httpResponse, handler);

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
        throw new IllegalArgumentException("handler adapter를 찾을 수 없습니다. handler=" + handler);
    }

    private Object getHandler(HttpRequest httpRequest) {
        String requestURI = httpRequest.getURI();
        if (isResourceURI(requestURI)) {
            return handlerMappingMap.get("resource");
        }
        return handlerMappingMap.get(requestURI);
    }

    private boolean isResourceURI(String uri) {
        return uri.contains(".css")
                || uri.contains("fonts")
                || uri.contains(".images")
                || uri.contains(".js")
                || uri.contains(".ico")
                || uri.contains(".html");
    }

}
