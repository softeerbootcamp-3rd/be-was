package webserver;

import java.io.*;
import java.lang.reflect.Method;
import java.net.Socket;

import common.util.FileManager;
import dto.HttpRequest;
import dto.HttpResponse;
import http.MethodMapper;
import http.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static dto.HttpResponse.*;
import static http.Status.*;

public class RequestHandler implements Runnable {
    public static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            DataOutputStream dos = new DataOutputStream(out);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            HttpRequest request = new HttpRequest(br);
            HttpResponse response = new HttpResponse();

            String requestMethod = request.getMethod();
            String requestPath = request.getPath();
            try {
                // 정적 컨텐츠 처리
                if (requestMethod.equals("GET")
                        && (FileManager.getFile(requestPath, getContentType(requestPath))) != null) {
                    response.makeBody(OK, requestPath);
                }
                // 동적 컨텐츠 처리
                else {
                    String endPoint = requestMethod + " " + requestPath;
                    Method method = MethodMapper.getMethod(endPoint);
                    response = (HttpResponse) method.invoke(
                            method.getDeclaringClass().getConstructor().newInstance(),
                            request.getBody());
                }
            } catch (Exception e) {
                ExceptionHandler.process(e, response);
            }

            ResponseHandler.send(dos, response);
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
    }
}
