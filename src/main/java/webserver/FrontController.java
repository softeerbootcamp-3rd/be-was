package webserver;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import controller.Controller;
import controller.ResourceController;
import controller.UserController;
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
    private Response res;
    private final Map<String, Controller> handlerMappingMap = new HashMap<>();
    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();
    public FrontController(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.req = new Request();
        this.res = new Response();
        initHandlerMappingMap();
        initHandlerAdapters();
    }

    private void initHandlerAdapters() {
        handlerAdapters.add(new MyHandlerAdapter());
    }

    private void initHandlerMappingMap() {
        handlerMappingMap.put("/user/*", new UserController());
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            getRequest(in);
            DataOutputStream dos = new DataOutputStream(out);
            Controller handler = getHandler(req);
            if (handler == null) {
                logger.debug("[RequestHandler.run] handler Not found");
                res.setStatus(HttpStatus.NOT_FOUND);
                res.send(dos,req);
                return;
            }
            MyHandlerAdapter adapter = getHandlerAdapter(handler);
            ModelAndView mv = adapter.handle(req, res, handler);
            MyView view = ViewResolver.resolve(mv.getViewName());
            view.render(dos, req, res);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private MyHandlerAdapter getHandlerAdapter(Controller handler) {
        for (MyHandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
        throw new IllegalArgumentException("handler adapter를 찾을 수 없습니다. handler=" + handler);
    }

    private Controller getHandler(Request req) {
        if (ViewResolver.isTemplate(req.getUrl())||ViewResolver.isStatic(req.getUrl())) {
            return new ResourceController();
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
