package webserver;

import java.io.*;
import java.net.Socket;

import dto.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        try {
            InputStream in = connection.getInputStream();
            OutputStream out = connection.getOutputStream();
            DataOutputStream dos = new DataOutputStream(out);

            execute(in, dos);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void execute(InputStream in, DataOutputStream dos) throws IOException {
        try {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpRequest httpRequest = new HttpRequest(connection, in);
            logger.debug(httpRequest.toString());

            Response response = RequestMappingHandler.handleRequest(httpRequest);
            HttpResponse.response(dos, response);
        } catch (IndexOutOfBoundsException e) {
            HttpResponse.response(dos, new Response(HttpStatus.BAD_REQUEST));
        } catch (Exception e) {
            HttpResponse.response(dos, new Response(HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

}