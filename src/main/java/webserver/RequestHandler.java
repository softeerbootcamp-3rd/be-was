package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

import http.response.HttpResponse;
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
            String requestUrl = httpRequest.getRequestLine().getUri();
            HttpResponse response;

            // "/" -> "/index.html"로 반환
            if (requestUrl.equals("/")) {
                requestUrl = "/index.html";
            }

            // 정적 자원부터 탐색 후 동적 자원 처리
            // 정적 자원 처리 - basePath 찾은 경우
            String basePath = FileReader.getBasePath(requestUrl);
            if (!basePath.isEmpty()) {
                Path filePath = Path.of(basePath + requestUrl);
                // 파일 존재할 경우 해당 파일 반환
                if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
                    // Request 헤더의 Accept 필드를 참고하여 반환할 타입 지정
                    String contentType = httpRequest.getEtcHeaders().get("Accept").split(",")[0];
                    byte[] body = FileReader.readFile(requestUrl);
                    response = HttpResponse.of(HttpStatus.OK, contentType, body);
                } else {
                    // 파일 존재하지 않을 경우 404 not found 반환
                    logger.info("404 not found");
                    response = HttpResponse.of(HttpStatus.NOT_FOUND);
                }
            } else {
                // 동적 자원 처리
                Function<HttpRequest, HttpResponse> controller = ControllerMapper.getController(httpRequest);
                response = controller.apply(httpRequest);
            }

            // 응답
            DataOutputStream dos = new DataOutputStream(out);
            response.send(dos, httpRequest);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
