package controller;

import config.AppConfig;
import controller.adapter.HandlerAdapter;
import controller.adapter.QnaControllerHandlerAdapter;
import controller.adapter.ResourceHandlerAdapter;
import controller.adapter.UserControllerHandlerAdapter;
import repository.QnaRepository;
import model.Qna;
import model.Request;
import model.Response;
import util.ResponseSender;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public class FrontController {
    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();
    static String DEFAULT_PAGE = "/templates/index.html";
    private final QnaRepository qnaRepository = AppConfig.qnaRepository();

    public FrontController() {
        initHandlerMappingMap();
        initHandlerAdapters();
    }

    private void initHandlerMappingMap() {
        // todo 제네릭으로 만들까?
        handlerMappingMap.put("css", new ResourceController("css"));
        handlerMappingMap.put("fonts", new ResourceController("fonts"));
        handlerMappingMap.put("images", new ResourceController("images"));
        handlerMappingMap.put("js", new ResourceController("js"));
        handlerMappingMap.put("ico", new ResourceController("ico"));
        handlerMappingMap.put("html", new ResourceController("html"));

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

    public void service(Request request, OutputStream out) throws IOException {
        Object handler = getHandler(request);
        Response response = new Response();

        HandlerAdapter adapter = getHandlerAdapter(handler);
        ModelView mv = adapter.handle(request, response, handler);

        String viewName = mv.getViewName();
        View view = viewResolver(viewName);

        if (request.getURI().contains("index.html")) {
            HashMap<String, Object> model = new HashMap<>();
            Collection<Qna> allQnas = qnaRepository.findAllQnas();
            model.put("{{qna-list}}", allQnas);
            mv.setModel(model);
        }

        if (adapter instanceof ResourceHandlerAdapter) {
            ResourceController resourceController = (ResourceController) handler;
            view.render(request, response, mv, resourceController.getType());
            ResponseSender.send(response, out);
            return;
        }

        view.render(request, response, mv);
        ResponseSender.send(response, out);
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
