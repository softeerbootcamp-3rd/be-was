package webserver;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import controller.MemberFormController;
import controller.StaticController;
import controller.TemplateController;
import http.HttpStatus;
import http.Request;
import http.Response;
import frontController.MyView;
import webserver.adaptor.MyHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FrontController implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(FrontController.class);

    private Socket connection;
    private Request req;

    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();
    public FrontController(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.req = new Request();
        initHandlerMappingMap();
        initHandlerAdapters();
    }

    private void initHandlerAdapters() {
        handlerAdapters.add(new MyHandlerAdapter());
    }

    private void initHandlerMappingMap() {
        handlerMappingMap.put("/user/create", new MemberFormController());
        handlerMappingMap.put("/*.html",new TemplateController());
        handlerMappingMap.put("/static/*",new StaticController());
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            getRequest(in);
            req.requestInfo();
            Response res = new Response();
            DataOutputStream dos = new DataOutputStream(out);

            //handler mapping
            Object handler = getHandler(req);

            if (handler == null) {
                logger.debug("[RequestHandler.run] handler Not found");
                byte[] body = new byte[0];
                res.setStatus(HttpStatus.NOT_FOUND);
                res.send(dos,body,req);
                return;
            }
            logger.debug("[RequestHandler.run] handler found : "+handler.getClass());
            MyHandlerAdapter adapter = getHandlerAdapter(handler);
            logger.debug("[RequestHandler.run] adapter found : "+adapter.getClass());
            ModelAndView mv = adapter.handle(req, res, handler);
            logger.debug("[RequestHandler.run] MV returned : "+mv);
            MyView view = viewResolver(mv.getViewName());
            logger.debug("[RequestHandler.run] view returned : "+view.getViewPath());
            view.render(dos, req, res);



        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private MyView viewResolver(String viewName) {
        if(isTemplate(viewName)||isStatic(viewName)){
            return new MyView(viewName);
        }

        return new MyView(viewName + ".html");
    }

    private MyHandlerAdapter getHandlerAdapter(Object handler) {
        for (MyHandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
        throw new IllegalArgumentException("handler adapter를 찾을 수 없습니다. handler=" + handler);
    }

    private Object getHandler(Request req) {
        String requestURI = req.getUrl();
        String normalizedURI = normalizeURI(requestURI);
        return handlerMappingMap.get(normalizedURI);
    }

    private String normalizeURI(String url) {
        // / 로 시작하고 .html 로 끝나는 경우만 처리
        if (isTemplate(url)) {
            return "/*.html";
        }
        else if(isStatic(url)){
            return "/static/*";
        }
        else {
            // 그 외의 경우는 그대로 반환
            return url;
        }
    }

    private void getRequest(InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line = bufferedReader.readLine();
        logger.debug("[RequestHandler.getRequest] line : "+line.split(" ")[1]);
        req.setMethod(line.split(" ")[0]);
        req.setUrl(line.split(" ")[1]);

        req.requestInfo();

    }

    private Boolean isTemplate(String url){
        return url.endsWith(".html");
    }
    private Boolean isStatic(String url){
        return url.startsWith("/css/")||url.startsWith("/fonts/")||url.startsWith("/images/")||url.startsWith("/js/");
    }
}
