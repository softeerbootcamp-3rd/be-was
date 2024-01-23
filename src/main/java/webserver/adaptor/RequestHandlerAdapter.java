package webserver.adaptor;

import annotation.GetMapping;
import annotation.RequestMapping;
import controller.BasicController;
import exception.GlobalExceptionHandler;
import http.Request;
import http.Response;
import controller.RequestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.ModelAndView;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RequestHandlerAdapter implements HandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerAdapter.class);
    @Override
    public boolean supports(BasicController handler) {
        return (handler instanceof RequestController);
    }

    @Override
    public ModelAndView handle(Request req, Response res, BasicController handler) {
        RequestController controller = (RequestController) handler;

        Map<String, String> model = new HashMap<>();
        String viewName;
        try {
            viewName = process(controller, req, model);

        } catch (Exception e) {
            viewName = GlobalExceptionHandler.handle(e, res);
        }
        ModelAndView mv = new ModelAndView(viewName);
        mv.setModel(model);

        return mv;
    }

    private String process(RequestController controller, Request req, Map<String, String> model) throws InvocationTargetException, IllegalAccessException {

        Class<?> clazz = controller.getClass();
        String prefix =clazz.getAnnotation(RequestMapping.class).value();
        String subPath = req.getUrl().replace(prefix, "");
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if(req.getMethod().equals("GET") && method.isAnnotationPresent(GetMapping.class)){
                if((method.getAnnotation(GetMapping.class).url()).equals(subPath)){
                    return (String) method.invoke(controller,req,model);
                }
            }
        }
        return "";
    }

}
