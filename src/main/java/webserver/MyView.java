package webserver;

import http.Request;
import http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.dispatcher.SimpleRequestDispatcher;

import java.io.DataOutputStream;
import java.io.IOException;

public class MyView {
    private static final Logger logger = LoggerFactory.getLogger(MyView.class);
    private String viewPath;
    public MyView(String viewPath) {
        this.viewPath = viewPath;
    }
    public void render(DataOutputStream dos,Request request, Response response) throws IOException {
        SimpleRequestDispatcher dispatcher = new SimpleRequestDispatcher();
        if(viewPath.startsWith("redirect:")){
            viewPath = viewPath.substring("redirect:".length());
            logger.debug("viewPath = {}",viewPath);
            dispatcher.sendRedirect(request,response,viewPath,dos);
            return;
        }
        logger.debug("viewPath = {}",viewPath);
        dispatcher.forward(request,response,viewPath,dos);
    }

    public String getViewPath() {
        return viewPath;
    }
}
