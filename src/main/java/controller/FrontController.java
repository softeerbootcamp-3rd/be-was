package controller;

import config.AppConfig;
import controller.adapter.HandlerAdapter;
import controller.adapter.QnaControllerHandlerAdapter;
import controller.adapter.ResourceHandlerAdapter;
import controller.adapter.UserControllerHandlerAdapter;
import repository.QnaRepository;
import model.Qna;
import model.HttpRequest;
import model.HttpResponse;
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

    public void service(HttpRequest httpRequest, OutputStream out) throws IOException {
        Object handler = getHandler(httpRequest);
        HttpResponse httpResponse = HttpResponse.newEmptyInstance();

        HandlerAdapter adapter = getHandlerAdapter(handler);
        ModelView mv = adapter.handle(httpRequest, httpResponse, handler);

        String viewName = mv.getViewName();
        View view = ViewResolver.resolveViewName(viewName);

        if (httpRequest.getURI().contains("index.html")) {
            HashMap<String, Object> model = new HashMap<>();
            Collection<Qna> allQnas = qnaRepository.findAllQnas();
            model.put("{{qna-list}}", allQnas);
            mv.setModel(model);
        }

        //todo 이걸 없애자
        if (adapter instanceof ResourceHandlerAdapter) {
            ResourceController resourceController = (ResourceController) handler;
            view.render(httpRequest, httpResponse, mv, resourceController.getType());
            ResponseSender.send(httpResponse, out);
            return;
        }

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
