package webserver.adaptor;

import annotation.GetMapping;
import annotation.RequestMapping;
import http.Request;
import http.Response;
import controller.BasicController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.ModelAndView;
import webserver.view.InternalResourceView;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MyHandlerAdapter implements HandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(MyHandlerAdapter.class);
    @Override
    public boolean supports(BasicController handler) {
        return (handler instanceof BasicController);
    }

    @Override
    public ModelAndView handle(Request req, Response res, BasicController handler) throws IOException, InvocationTargetException, IllegalAccessException {
        BasicController controller = handler;

        Map<String, String> model = new HashMap<>();
        String viewName = process(controller,req,model);
        ModelAndView mv = new ModelAndView(viewName);
        mv.setModel(model);

        return mv;
    }

    private String process(BasicController controller, Request req, Map<String, String> model) throws InvocationTargetException, IllegalAccessException {
        logger.debug("controller = {}",controller);
        logger.debug("req.getMethod() = {}",req.getMethod());
        logger.debug("req.getURL() = {}",req.getUrl());
        Class<?> clazz = controller.getClass();
        String prefix =clazz.getAnnotation(RequestMapping.class).value();
        logger.debug("prefix = {}",prefix);
        String subPath = req.getUrl().replace(prefix, "");
        logger.debug("subPath = {}",subPath);
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if(req.getMethod().equals("GET") && method.isAnnotationPresent(GetMapping.class)){
                logger.debug("method.getAnnotation(GetMapping.class).url() = {}",method.getAnnotation(GetMapping.class).url());
                if((method.getAnnotation(GetMapping.class).url()).equals(subPath)){
                    return (String) method.invoke(controller,req,model);
                }
            }
        }
        return "";
    }

}
