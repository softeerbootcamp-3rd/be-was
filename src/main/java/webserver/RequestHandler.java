package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

import http.response.HttpResponse;
import http.ContentType;
import http.request.HttpRequest;
import http.HttpStatus;
import utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileReader;

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
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            // httpRequest 생성 및 출력
            HttpRequest httpRequest = new HttpRequest(br);
            logger.debug(httpRequest.toString());

            // 동적 & 정적 자원 분할 처리
            Function<HttpRequest, HttpResponse> controller = ControllerMapper.getController(httpRequest);
            HttpResponse response;
            String requestUrl = httpRequest.getRequestLine().getUri();
            // 동적 자원 처리
            if (controller != null) {
                logger.info("controller 호출");
                response = controller.apply(httpRequest);
            } else {
                // 정적 자원 처리
                logger.info("정적 파일 호출");
                if (requestUrl.equals("/")) {
                    requestUrl = "/index.html";
                }

                String basePath = FileReader.getBasePath(requestUrl);
                Path filePath = Path.of(basePath + requestUrl);
                if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
                    String extension = FileReader.getFileExtension(filePath);
                    byte[] body = FileReader.readFile(requestUrl);
                    response = HttpResponse.of(HttpStatus.OK, ContentType.getContentType(extension), body);
                } else {
                    logger.info("404 not found");
                    response = HttpResponse.of(HttpStatus.NOT_FOUND);
                }
            }

            // 응답
            DataOutputStream dos = new DataOutputStream(out);
            response.send(dos, httpRequest);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}