package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.request.HttpRequest;
import webserver.http.request.HttpRequestParser;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseSender;
import webserver.routing.DynamicRoutingManager;
import webserver.routing.StaticRoutingManager;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static utils.HttpLogger.requestLogging;
import static utils.HttpLogger.responseLogging;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpRequest httpRequest = HttpRequestParser.getInstance().parse(in);
            requestLogging(httpRequest);

            DataOutputStream dos = new DataOutputStream(out);
            HttpResponse httpResponse;

            // 동적 로직 라우팅
            httpResponse = DynamicRoutingManager.getInstance().handleRequest(httpRequest);
            // 정적 로직 라우팅
            if(httpResponse == null){
                httpResponse = StaticRoutingManager.getInstance().handleRequest(httpRequest);
            }

            responseLogging(httpResponse);
            new HttpResponseSender().sendResponse(httpResponse, dos);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
