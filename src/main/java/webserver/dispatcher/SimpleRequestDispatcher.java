package webserver.dispatcher;

import http.HttpStatus;
import http.Request;
import http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.FrontController;

import java.io.File;
import java.io.IOException;
import java.io.DataOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SimpleRequestDispatcher implements RequestDispatcher{
    private static final Logger logger = LoggerFactory.getLogger(FrontController.class);
    @Override
    public void forward(Request request, Response response, String viewPath,DataOutputStream dos) throws IOException {
        File file = new File(getAbsolutePath(viewPath));
        byte[] body;
        if (file.exists() && file.isFile()) {
            body = Files.readAllBytes(file.toPath());
            response.setStatus(HttpStatus.OK);
        }
        else{
            body = Files.readAllBytes(new File(getAbsolutePath("/not-found.html")).toPath());
            response.setStatus(HttpStatus.NOT_FOUND);
        }
        response.send(dos,body,request);
    }

    @Override
    public void sendRedirect(Request request, Response response, String viewPath, DataOutputStream dos) {
        byte[] body = new byte[0];
        response.setLocation(viewPath);
        response.setStatus(HttpStatus.MOVED_PERMANENTLY);
        response.send(dos,body,request);
    }

    private String getAbsolutePath(String viewPath){
        if(viewPath.endsWith(".html")){
            return Paths.get(System.getProperty("user.dir"), "src/main/resources/templates").toString()+"/"+viewPath;
        }
        else{
            return Paths.get(System.getProperty("user.dir"), "src/main/resources/static").toString() + viewPath;
        }
    }

}
