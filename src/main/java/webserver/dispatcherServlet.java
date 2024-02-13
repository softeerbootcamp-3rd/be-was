package webserver;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import annotation.Controller;
import annotation.RequestMapping;
import com.sun.tools.javac.Main;
import controller.BasicController;
import controller.MainController;
import http.Request;
import http.Response;
import utils.ClassScanner;
import webserver.adaptor.HandlerAdapter;
import webserver.adaptor.RequestHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.view.*;

public class dispatcherServlet implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(dispatcherServlet.class);

    private Socket connection;
    private final Map<String, BasicController> handlerMappingMap = new HashMap<>();
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
            handlerMappingMap.put(path+"/*", (BasicController) clazz.getDeclaredConstructor().newInstance());

        }

    }

    public void run() {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            Request req = new Request(in);
            DataOutputStream dos = new DataOutputStream(out);
            Response res = new Response(dos);
            BasicController handler = getHandler(req);
            if (handler == null) {
                String filePath = ViewResolver.getAbsolutePath(req.getUrl());
                InternalResourceView view = new InternalResourceView(filePath);
                view.render(req,res,null);
                return;
            }


            HandlerAdapter adapter = getHandlerAdapter(handler);
            ModelAndView mv = adapter.handle(req, res, handler);

            View view = ViewResolver.resolve(mv.getViewName());
            view.render(req, res, mv.getModel());

        }
        catch (Exception e) {
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

    private BasicController getHandler(Request req) {
        if(ViewResolver.isTemplate(req.getUrl())||ViewResolver.isStatic(req.getUrl())){
            return null;
        }
        BasicController result = null;
        String resultSubPath = req.getUrl();
        for (String key : handlerMappingMap.keySet()) {
            if(isPatternMatch(key,req.getUrl())){
                String compare = req.getUrl().replace(key.replace("/*",""),"");
                result = (result!=null && resultSubPath.length() < compare.length())?
                        result:handlerMappingMap.get(key);
                resultSubPath = (result != null && resultSubPath.length() < compare.length())
                        ? resultSubPath : compare;
            }
        }
        return result;
    }

    private boolean isPatternMatch(String pattern, String path) {
        pattern = pattern.replace("*",".*");
        return path.matches(pattern);
    }

}
