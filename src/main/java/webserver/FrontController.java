package webserver;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import controller.UserController;
import controller.StaticController;
import controller.TemplateController;
import http.HttpStatus;
import http.Request;
import http.Response;
import webserver.adaptor.MyHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FrontController implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(FrontController.class);

    private Socket connection;
    private Request req;

    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();
    private final ViewResolver viewResolver = new ViewResolver();
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

        handlerMappingMap.put("/*.html",new TemplateController());
        handlerMappingMap.put("/static/*",new StaticController());
        handlerMappingMap.put("/user/*", new UserController());
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
            MyHandlerAdapter adapter = getHandlerAdapter(handler);
            ModelAndView mv = adapter.handle(req, res, handler);
            MyView view = viewResolver.resolve(mv.getViewName());
            view.render(dos, req, res);



        } catch (IOException e) {
            logger.error(e.getMessage());
        }
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
        if (viewResolver.isTemplate(req.getUrl())) {
            return handlerMappingMap.get("/*.html");
        }
        else if(viewResolver.isStatic(req.getUrl())){
            return handlerMappingMap.get("/static/*");
        }
        for (String key : handlerMappingMap.keySet()) {
            if(isPatternMatch(key,req.getUrl())){
                return handlerMappingMap.get(key);
            }
        }
        return null;
    }

    private boolean isPatternMatch(String pattern, String path) {
        pattern = pattern.replace("*",".*");
        return path.matches(pattern);
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


}
