package controller;

import controller.adapter.HandlerAdapter;
import controller.adapter.StaticResourceHandlerAdapter;
import controller.adapter.UserControllerHandlerAdapter;
import controller.user.UserCreateController;
import controller.user.UserFormController;
import controller.user.UserLoginController;
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
    static String DEFAULT_PAGE = "/templates/index.html";

    public FrontController() {
        initHandlerMappingMap();
        initHandlerAdapters();
    }

    private void initHandlerMappingMap() {
        handlerMappingMap.put("css", new StaticResourceController("css"));
        handlerMappingMap.put("fonts", new StaticResourceController("fonts"));
        handlerMappingMap.put("images", new StaticResourceController("images"));
        handlerMappingMap.put("js", new StaticResourceController("js"));
        handlerMappingMap.put("ico", new StaticResourceController("ico"));

        handlerMappingMap.put("/user/form.html", new UserFormController());
        handlerMappingMap.put("/user/create", new UserCreateController());
        handlerMappingMap.put("/user/login.html", new UserLoginController());

        // qna 추가
    }

    private void initHandlerAdapters() {
        handlerAdapters.add(new StaticResourceHandlerAdapter());
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

        if (adapter instanceof StaticResourceHandlerAdapter) {
            StaticResourceController staticResourceController = (StaticResourceController) handler;
            view.render(request, out, staticResourceController.getType());
            return;
        }
        view.render(request, out);
    }

    private View viewResolver(String viewName) {
        return new View("./src/main/resources" + viewName);
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
        if (isResourceURI(requestURI)) {
            return handlerMappingMap.get(extractResourceType(requestURI));
        }
        return handlerMappingMap.get(requestURI);
    }

    private String extractResourceType(String requestURI) {
        if (requestURI.contains("css"))
            return "css";
        if (requestURI.contains("fonts"))
            return "fonts";
        if (requestURI.contains("images"))
            return "images";
        if (requestURI.contains("js"))
            return "js";
        if (requestURI.contains("ico"))
            return "ico";

        return "";
    }

    private boolean isResourceURI(String uri) {
        return uri.contains(".css")
                || uri.contains("fonts")
                || uri.contains(".images")
                || uri.contains(".js")
                || uri.contains(".ico");
    }

}
