package controller;

import controller.adapter.HandlerAdapter;
import controller.adapter.QnaControllerHandlerAdapter;
import controller.adapter.ResourceHandlerAdapter;
import controller.adapter.UserControllerHandlerAdapter;
import controller.qna.QnaCreateController;
import controller.qna.QnaFormController;
import controller.user.UserCreateController;
import controller.user.UserListController;
import controller.user.UserLoginController;
import db.Database;
import model.Qna;
import model.Request;
import model.Response;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

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
        handlerMappingMap.put("/user/login", new UserLoginController());
        handlerMappingMap.put("/user/list", new UserListController());

        handlerMappingMap.put("/qna/form", new QnaFormController());
        handlerMappingMap.put("/qna/create", new QnaCreateController());
    }

    private void initHandlerAdapters() {
        handlerAdapters.add(new ResourceHandlerAdapter());
        handlerAdapters.add(new UserControllerHandlerAdapter());
        handlerAdapters.add(new QnaControllerHandlerAdapter());
    }

    public void service(Request request, OutputStream out) throws IOException {
        Object handler = getHandler(request);
        Response response = new Response();

        HandlerAdapter adapter = getHandlerAdapter(handler);
        ModelView mv = adapter.handle(request, response, handler);

        String viewName = mv.getViewName();
        View view = viewResolver(viewName);

        if (request.getURI().contains("index.html")) {
            HashMap<String, Object> model = new HashMap<>();
            Collection<Qna> allQnas = Database.findAllQnas();
            model.put("{{qna-list}}", allQnas);
            mv.setModel(model);
        }

        if (adapter instanceof ResourceHandlerAdapter) {
            ResourceController resourceController = (ResourceController) handler;
            view.render(request, response, mv, resourceController.getType());
            response.send(out);
            return;
        }

        view.render(request, response, mv);
        response.send(out);
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
