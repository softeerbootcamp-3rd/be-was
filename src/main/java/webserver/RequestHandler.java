package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;

import httpmessage.Request.HttpMesssageReader;
import httpmessage.Request.HttpRequest;
import httpmessage.Response.HttpResponse;
import httpmessage.Request.RequestHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.*;

public class RequestHandler implements Runnable {
    private Socket connection;
    private final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            final HttpResponse httpResponse = new HttpResponse();
            final HttpRequest httpRequest = new HttpRequest(new HttpMesssageReader(br));

            String path = httpRequest.getPath();
            String method = httpRequest.getHttpMethod();

            logger.debug(">>  {} >> {}", method, path);

            final FirstService firstService = new FirstService();
            firstService.service(httpRequest,httpResponse);

            DataOutputStream dos = new DataOutputStream(out);
            responseHeader(dos, httpResponse);
            responseBody(dos, httpResponse);

        }
        catch (IOException e) {
            logger.error(e.getMessage());
        }
    }


    private void responseHeader(DataOutputStream dos, HttpResponse httpResponse) throws IOException {
        dos.writeBytes("HTTP/1.1 " + httpResponse.getStatusCode() + " " + httpResponse.getStatusLine()+"\r\n");
        dos.writeBytes("Content-Type: " + httpResponse.getContentType() + "\r\n");
        dos.writeBytes("Content-Length: " + httpResponse.getBody().length + "\r\n");
        dos.writeBytes("\r\n");
    }
    private void responseBody(DataOutputStream dos, HttpResponse httpResponse) {
        try {
            dos.write(httpResponse.getBody(), 0, httpResponse.getBody().length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
