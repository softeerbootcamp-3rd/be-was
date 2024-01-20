package webserver;

import java.io.*;
import java.net.Socket;
import java.util.function.Function;

import model.HttpRequest;
import model.HttpResponse;
import util.URLMapper;

import static constant.HttpRequestConstant.*;
import static webserver.HttpStatus.OK;

public class RequestHandler implements Runnable {
    private Socket connection;
    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpRequest httpRequest = HttpRequest.from(in);
            logger.debug(httpRequest.toString());
            Function<HttpRequest, HttpResponse> controller = URLMapper.getController(httpRequest);

            if(controller == null){
                HttpResponse httpResponse = HttpResponse.response200(OK, httpRequest.getPath(), httpRequest.getExtension());
                httpResponse.send(out);
                return;
            }

            HttpResponse httpResponse = controller.apply(httpRequest);
            httpResponse.send(out);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
