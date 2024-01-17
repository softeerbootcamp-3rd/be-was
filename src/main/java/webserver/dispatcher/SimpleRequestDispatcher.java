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
        logger.debug("[ start forwarding ]");
        logger.debug("viewPath : "+viewPath);
        byte[] body = Files.readAllBytes(new File(getAbsolutePath(viewPath)).toPath());
        logger.debug("set body in forward");
        response.send(dos,body,request);
    }

    @Override
    public void sendRedirect(Request request, Response response, String viewPath, DataOutputStream dos) {
        byte[] body = new byte[0];
        response.setLocation(viewPath);
        response.setStatus(HttpStatus.MOVED_PERMANENTLY);
        logger.debug("[ response ]");
        logger.debug("location : "+response.getLocation());
        logger.debug("status : "+response.getStatus());
        response.send(dos,body,request);
    }

    private String getAbsolutePath(String viewPath){
        if(viewPath.endsWith(".html")){
            return Paths.get(System.getProperty("user.dir"), "src/main/resources/templates").toString()+"/"+viewPath;
        }
        else{
            return Paths.get(System.getProperty("user.dir"), "src/main/resources/static").toString() + "/" + viewPath;
        }
    }

}
