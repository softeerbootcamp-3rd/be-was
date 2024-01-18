package controller;

import controller.adapter.HandlerAdapter;
import controller.adapter.UserControllerHandlerAdapter;
import controller.user.UserCreateController;
import controller.user.UserFormController;
import model.Request;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FrontController {
    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();
    static String DEFAULT_PAGE = "templates/index.html";

    public FrontController() {
        initHandlerMappingMap();
        initHandlerAdapters();
    }

    private void initHandlerMappingMap() {
        handlerMappingMap.put("/user/form.html", new UserFormController());
        handlerMappingMap.put("/user/create", new UserCreateController());

        // qna 추가
    }

    private void initHandlerAdapters() {
        handlerAdapters.add(new UserControllerHandlerAdapter());

        //qna 추가
    }

    public void service(Request request, OutputStream out) throws IOException {
        Object handler = getHandler(request);

        if (handler == null) {
            View view = viewResolver(DEFAULT_PAGE);
            view.render(request, out);
            return;
        }

        HandlerAdapter adapter = getHandlerAdapter(handler);
        ModelView mv = adapter.handle(request, handler);

        String viewName = mv.getViewName();
        View view = viewResolver(viewName);

        view.render(request, out);
    }

    private View viewResolver(String viewName) {
        return new View("./src/main/resources/" + viewName);
    }

    private HandlerAdapter getHandlerAdapter(Object handler) {
        for (HandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
        throw new IllegalArgumentException("handler adapter를 찾을 수 없습니다. handler=" + handler);
    }

    private Object getHandler(Request request) {
        String requestURI = request.getURI();
        return handlerMappingMap.get(requestURI);
    }

}
