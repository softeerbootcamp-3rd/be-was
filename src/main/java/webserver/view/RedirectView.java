package webserver.view;

import http.HttpContentType;
import http.HttpStatus;
import http.Request;
import http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.dispatcher.dispatcherServlet;

import java.io.DataOutputStream;

public class RedirectView implements View{
    private static final Logger logger = LoggerFactory.getLogger(RedirectView.class);
    String viewPath;

    public RedirectView(String viewPath) {
        this.viewPath = viewPath;
    }

    @Override
    public String getContentType() {
        int dotIndex = viewPath.lastIndexOf(".");
        if (dotIndex != -1 && dotIndex < viewPath.length() - 1) {
            String fileExtension = viewPath.substring(dotIndex + 1);
            return HttpContentType.getValue(fileExtension);
        }
        return null;
    }

    @Override
    public void render(DataOutputStream dos, Request request, Response response) throws Exception {
        viewPath = viewPath.substring("redirect:".length());
        logger.debug("viewPath = {}",viewPath);
        sendRedirect(request,response,viewPath,dos);
    }

    private void sendRedirect(Request request, Response response, String viewPath, DataOutputStream dos) {
        response.setLocation(viewPath);
        response.setStatus(HttpStatus.MOVED_PERMANENTLY);
        response.send(dos,request);
    }
}
