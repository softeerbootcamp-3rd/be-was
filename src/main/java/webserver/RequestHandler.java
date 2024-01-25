package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Map;
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
                String filePath = basePath + requestUrl;
                File file = new File(filePath);
                // 파일 존재할 경우 해당 파일 반환
                if (file.exists() && file.isFile()) {
                    byte[] body = FileReader.readFile(file);

                    // Request 헤더의 Accept 필드를 참고하여 반환할 타입 지정
                    String contentType = httpRequest.getEtcHeaders().get("Accept").split(",")[0];
                    response = HttpResponse.of(HttpStatus.OK, contentType, body);
                } else {
                    // 파일 존재하지 않을 경우 404 not found 반환
                    logger.info("404 not found");
                    response = HttpResponse.of(HttpStatus.NOT_FOUND);
                }
            } else {
                // 동적 자원 처리
                // method + path 갖고오기
                String requestURL = httpRequest.getRequestLine().getMethodAndPath();
                logger.debug("requestURL: {}", requestURL);
                // 해당 값으로 Validator, Controller 탐색
                ValidatorController vc = ValidatorController.getValidatorController(requestURL);
                Function<Map<String, String>, Boolean> validator = vc.getValidator();
                // 유효성 검증 통과한 경우 컨트롤러에 값 전달
                if (validator.apply(httpRequest.getBody())) {
                    Function<Map<String, String>, HttpResponse> controller = vc.getController();
                    response = controller.apply(httpRequest.getBody());
                } else {
                    // 유효성 검증 실패할 경우 BAD_REQUEST 반환
                    response = HttpResponse.of(HttpStatus.BAD_REQUEST);
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
