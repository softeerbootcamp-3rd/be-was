package webserver;

import controller.MainController;
import model.Request;
import model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class HttpHandler implements Runnable{
    private Socket connection;
    private static final Logger logger = LoggerFactory.getLogger(HttpHandler.class);
    public HttpHandler(Socket connectionSocket) {this.connection = connectionSocket;}
    public void run() {
        logger.debug("#####  New Client Connect! Connected IP : {}, Port : {}  #####",
                connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream()) {
            DataOutputStream dos = new DataOutputStream(out);
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            RequestHandler requestHandler = new RequestHandler(br);
            Request request = requestHandler.handleRequest();
            Response response = MainController.control(request);
            ResponseHandler responseHandler = new ResponseHandler(dos);
            responseHandler.handleResponse(response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
