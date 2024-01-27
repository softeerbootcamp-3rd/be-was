package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.RequestParser;


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
                // TODO 분석한 요청의 경로를 이용해, 로그인한 사용자만 사용 가능한 건지 확인 후 처리
                DataOutputStream dos = new DataOutputStream(out);
                HttpResponse httpResponse;

                // 정적 요청 처리
                httpResponse = StaticHandler.handle(httpRequest);
                // 동적 요청 처리
                if (httpResponse == null) {
                    httpResponse = DynamicHandler.handle(httpRequest);
                }

                ResponseHandler.send(dos, httpResponse);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
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
