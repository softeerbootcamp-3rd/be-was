package webserver;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import annotation.Controller;
import annotation.RequestMapping;
import controller.BasicController;
import http.HttpStatus;
import http.Request;
import http.Response;
import utils.ClassScanner;
import webserver.adaptor.MyHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.view.View;

public class dispatcherServlet implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(dispatcherServlet.class);

    private Socket connection;
    private Request req;
    private Response res;
    private final Map<String, BasicController> handlerMappingMap = new HashMap<>();
    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();
    public dispatcherServlet(Socket connectionSocket) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        this.connection = connectionSocket;
        this.req = new Request();
        this.res = new Response();
        initHandlerMappingMap();
        initHandlerAdapters();
    }

    private void initHandlerAdapters() {
        handlerAdapters.add(new MyHandlerAdapter());
    }

    private void initHandlerMappingMap() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        for (Class<?> clazz : ClassScanner.findClasses("controller")) {
            RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
            logger.debug("clazz = {}",clazz);
            if(clazz.isInterface() || requestMapping==null || !clazz.isAnnotationPresent(Controller.class)){
                continue;
            }

            String path = clazz.getAnnotation(RequestMapping.class).value();
            logger.debug("path = {}",path+"/*");
            handlerMappingMap.put(path+"/*", (BasicController) clazz.getDeclaredConstructor().newInstance());

        }

    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            getRequest(in);
            DataOutputStream dos = new DataOutputStream(out);

            if (ViewResolver.isTemplate(req.getUrl())||ViewResolver.isStatic(req.getUrl())) {
                byte[] body = Files.readAllBytes(new File(ViewResolver.getAbsolutePath(req.getUrl())).toPath());
                res.send(dos,body,req);
            }

            BasicController handler = getHandler(req);
            if (handler == null) {
                logger.debug("[RequestHandler.run] handler Not found");
                res.setStatus(HttpStatus.NOT_FOUND);
                res.send(dos,req);
                return;
            }
            MyHandlerAdapter adapter = getHandlerAdapter(handler);
            ModelAndView mv = adapter.handle(req, res, handler);
            logger.debug("mv.getViewName() = {}",mv.getViewName());
            View view = ViewResolver.resolve(mv.getViewName());
            logger.debug("view = {}",view);
            view.render(dos, req, res);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private MyHandlerAdapter getHandlerAdapter(BasicController handler) {
        for (MyHandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
        throw new IllegalArgumentException("handler adapter를 찾을 수 없습니다. handler=" + handler);
    }

    private BasicController getHandler(Request req) {
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



    private static boolean isControllerClass(Class<?> clazz) {
        return BasicController.class.isAssignableFrom(clazz) && !clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers());
    }


}
