package webserver;

import webserver.header.RequestHeader;
import handler.GetRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import parser.GetRequestParser;
import parser.RequestHeaderParser;
import webserver.response.Response;
import webserver.status.HttpStatus;
import webserver.type.ContentType;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final URL RESOURCES_URL = this.getClass().getClassLoader().getResource("./templates");
    private Socket connection;

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

            sendResponse(dos, requestHeader);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void sendResponse(DataOutputStream dos, RequestHeader requestHeader) {
        Response response = null;

        try{
            File file = new File(RESOURCES_URL.getPath() + requestHeader.getPath());

            if(file.exists()) {
                response = Response.onSuccess(ContentType.HTML, Files.readAllBytes(file.toPath()));
            }

            if(requestHeader.getMethod().equals("GET")){
                Object result = GetRequestHandler.run(GetRequestParser.parse(requestHeader.getPath()));

                if(result instanceof Response){
                    response = (Response) result;
                }
            }

        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException | IOException e){
            response = Response.onFailure(HttpStatus.NOT_FOUND, ContentType.HTML, "404 Not Found".getBytes());

            logger.error(e.getMessage());
        } finally {
            setResponse(dos, response);
        }
    }

    private void setResponse(DataOutputStream dos, Response response) {
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
