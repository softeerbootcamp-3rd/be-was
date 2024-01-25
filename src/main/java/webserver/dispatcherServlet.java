package webserver;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import annotation.Controller;
import annotation.RequestMapping;
import controller.BasicController;
import controller.RequestController;
import http.Cookie;
import http.Request;
import http.Response;
import http.SessionManager;
import utils.ClassScanner;
import webserver.adaptor.HandlerAdapter;
import webserver.adaptor.RequestHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.view.InternalResourceView;
import webserver.view.RedirectView;
import webserver.view.View;

public class dispatcherServlet implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(dispatcherServlet.class);

    private Socket connection;
    private final Map<String, RequestController> handlerMappingMap = new HashMap<>();
    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();
    public dispatcherServlet(Socket connectionSocket) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        this.connection = connectionSocket;
        initHandlerMappingMap();
        initHandlerAdapters();
    }

    private void initHandlerAdapters() {
        handlerAdapters.add(new RequestHandlerAdapter());
    }

    private void initHandlerMappingMap() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        for (Class<?> clazz : ClassScanner.findClasses("controller")) {
            RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
            if(clazz.isInterface() || requestMapping==null || !clazz.isAnnotationPresent(Controller.class)){
                continue;
            }

            String path = clazz.getAnnotation(RequestMapping.class).value();
            handlerMappingMap.put(path+"/*", (RequestController) clazz.getDeclaredConstructor().newInstance());

        }

    }

    public void run() {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            Request req = new Request(in);
            DataOutputStream dos = new DataOutputStream(out);
            Response res = new Response(dos);
            RequestController handler = getHandler(req);
            if (handler == null) {
                String filePath = ViewResolver.getAbsolutePath(req.getUrl());
                View view = new InternalResourceView(filePath);
                view.render(req,res);
                return;
            }
            HandlerAdapter adapter = getHandlerAdapter(handler);
            ModelAndView mv = adapter.handle(req, res, handler);
            View view = ViewResolver.resolve(mv.getViewName());
            view.render(req, res);
        } catch (Exception e) {
            logger.error("e.getMessage() = {}",e.getMessage());
            e.printStackTrace();
        }
    }

    private HandlerAdapter getHandlerAdapter(BasicController handler) {
        for (HandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
        throw new IllegalArgumentException("handler adapter를 찾을 수 없습니다. handler=" + handler);
    }

    private RequestController getHandler(Request req) {
        if(ViewResolver.isTemplate(req.getUrl())||ViewResolver.isStatic(req.getUrl())){
            return null;
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

}
