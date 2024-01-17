package webserver;

import java.io.*;
import java.net.Socket;
import java.util.function.Function;

import dto.RequestBuilder;
import dto.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.ControllerMapper;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.

            HttpRequest httpRequest = new HttpRequest(connection, in);
            logger.debug(httpRequest.toString());
            DataOutputStream dos = new DataOutputStream(out);

            // GET만 다루고 있으므로 일단 body는 null로
            RequestBuilder requestBuilder = new RequestBuilder<>(httpRequest.getUri(), null);

            Function<RequestBuilder, ResponseBuilder> controller =
                    ControllerMapper.getController(httpRequest.getHttpMethod());

            if (controller != null) {
                ResponseBuilder responseBuilder = controller.apply(requestBuilder);
                HttpResponse.response(dos, responseBuilder);
            } else {
                HttpResponse.response(dos, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
