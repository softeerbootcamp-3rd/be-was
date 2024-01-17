package webserver;

import http.Request;
import http.Response;
import webserver.dispatcher.SimpleRequestDispatcher;

import java.io.DataOutputStream;
import java.io.IOException;

public class MyView {
    private String viewPath;
    public MyView(String viewPath) {
        this.viewPath = viewPath;
    }
    public void render(DataOutputStream dos,Request request, Response response) throws IOException {
        SimpleRequestDispatcher dispatcher = new SimpleRequestDispatcher();
        if(viewPath.startsWith("redirect:")){
            viewPath = viewPath.substring("redirect:".length());
            dispatcher.sendRedirect(request,response,viewPath,dos);
            return;
        }
        dispatcher.forward(request,response,viewPath,dos);
    }

    public String getViewPath() {
        return viewPath;
    }
}
