package webserver;

import http.Request;
import http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.dispatcher.servletDispatcher;

import java.io.DataOutputStream;
import java.io.IOException;

public class MyView {
    private static final Logger logger = LoggerFactory.getLogger(MyView.class);
    private String viewPath;
    public MyView(String viewPath) {
        this.viewPath = viewPath;
    }
    public void render(DataOutputStream dos,Request request, Response response) throws IOException {
        if(viewPath.startsWith("redirect:")){
            viewPath = viewPath.substring("redirect:".length());
            logger.debug("viewPath = {}",viewPath);
            servletDispatcher.sendRedirect(request,response,viewPath,dos);
            return;
        }
        logger.debug("viewPath = {}",viewPath);
        servletDispatcher.forward(request,response,viewPath,dos);
    }

    public String getViewPath() {
        return viewPath;
    }
}
