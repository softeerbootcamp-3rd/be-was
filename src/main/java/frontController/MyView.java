package frontController;

import dto.Request;
import dto.Response;
import frontController.dispatcher.SimpleRequestDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

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
