package webserver.handler;

import webserver.header.RequestHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.parser.GetRequestParser;
import webserver.parser.RequestHeaderParser;
import webserver.response.Response;
import webserver.status.HttpStatus;
import webserver.type.ContentType;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);

            RequestHeader requestHeader = RequestHeaderParser.parse(in);

            Response response = handleRequest(requestHeader);
            sendResponse(dos, response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private Response handleRequest(RequestHeader requestHeader) {
        Response response = null;

        try{
            if(requestHeader.getMethod().equals("GET")){
                response = ResourceHandler.run(requestHeader);

                if(response == null){
                    Object result = GetRequestHandler.run(GetRequestParser.parse(requestHeader.getPath()));

                    if (result instanceof Response) {
                        response = (Response) result;
                    }
                }
            }

            return response;
        } catch (Throwable e){
            logger.error(e.getMessage());
            response = ExceptionHandler.handle(e);

            return response;
        }
    }

    private void sendResponse(DataOutputStream dos, Response response) {
        try {
            if(response == null){
                throw new IllegalArgumentException("");
            }

            HttpStatus httpStatus = response.getHttpStatus();
            ContentType contentType = response.getContentType();
            byte[] body = response.getBody();

            dos.writeBytes(String.format("HTTP/1.1 %d %s \r\n", httpStatus.getCode(), httpStatus.getName()));
            dos.writeBytes(String.format("Content-Type: %s;charset=utf-8\r\n", contentType.getValue()));
            dos.writeBytes(String.format("Content-Length: %d\r\n", body.length));
            dos.writeBytes("\r\n");

            dos.write(body, 0, body.length);

            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
