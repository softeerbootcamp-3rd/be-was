package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.RequestParser;
import webserver.request.ApiHandler;
import webserver.request.FileContentHandler;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.response.ResponseHandler;


public class Dispatcher implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);

    private Socket connection;

    public Dispatcher(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // 유효한 연결인지 확인
            if (isValidConnection(in, out)) {

                HttpRequest httpRequest = RequestParser.getHttpRequest(in);
                ThreadLocalManager.setSession(httpRequest.getSessionId());

                HttpResponse httpResponse;
                // API 호출 요청인지 확인
                httpResponse = ApiHandler.handle(httpRequest);
                // 파인 컨텐츠 요청인지 확인 후 처리
                if (httpResponse == null) {
                    httpResponse = FileContentHandler.handle(httpRequest);
                }

                DataOutputStream dos = new DataOutputStream(out);
                ResponseHandler.send(dos, httpResponse);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            ThreadLocalManager.clear();
        }
    }

    private static boolean isValidConnection(InputStream in, OutputStream out) throws IOException {
        if (in == null || out == null) {
            if (in != null) in.close();
            if (out != null) out.close();
            return false;
        }
        return true;
    }

}
