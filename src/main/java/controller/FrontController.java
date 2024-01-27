package controller;

import controller.adapter.HandlerAdapter;
import controller.adapter.ResourceHandlerAdapter;
import controller.adapter.UserControllerHandlerAdapter;
import controller.user.UserCreateController;
import controller.user.UserLoginController;
import model.Request;
import model.Response;

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
        handlerMappingMap.put("css", new ResourceController("css"));
        handlerMappingMap.put("fonts", new ResourceController("fonts"));
        handlerMappingMap.put("images", new ResourceController("images"));
        handlerMappingMap.put("js", new ResourceController("js"));
        handlerMappingMap.put("ico", new ResourceController("ico"));
        handlerMappingMap.put("html", new ResourceController("html"));

        handlerMappingMap.put("/user/create", new UserCreateController());

        // qna 추가
    }

    private void initHandlerAdapters() {
        handlerAdapters.add(new ResourceHandlerAdapter());
        handlerAdapters.add(new UserControllerHandlerAdapter());

        //qna 추가
    }

    public void service(Request request, OutputStream out) throws IOException {
        Object handler = getHandler(request);
        Response response = new Response();

        if (handler == null) {
            View view = viewResolver(DEFAULT_PAGE);
            view.render(request, response, out);
            return;
        }

        HandlerAdapter adapter = getHandlerAdapter(handler);
        ModelView mv = adapter.handle(request, response, handler);

        String viewName = mv.getViewName();
        View view = viewResolver(viewName);

        if (adapter instanceof ResourceHandlerAdapter) {
            ResourceController resourceController = (ResourceController) handler;
            view.render(request, response, out, resourceController.getType());
            return;
        }
        view.render(request, response, out);
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
        if (requestURI.contains("html"))
            return "html";

        return "";
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
