package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.function.Function;

import http.request.RequestLine;
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
                response = ResourceHandler.process(filePath, httpRequest);
            } else {
                // 동적 자원 처리
                RequestLine requestLine = httpRequest.getRequestLine();
                ValidatorControllerMapper vc = ValidatorControllerMapper.getValidatorAndControllerByPath(requestLine.getPath());

                if (vc != null) {
                    Function<Map<String, String>, Boolean> validator = vc.getValidator();
                    // 유효성 검증 실패할 경우 BAD_REQUEST 반환
                    if (validator != null && !validator.apply(httpRequest.getBody())) {
                        response = HttpResponse.of(HttpStatus.BAD_REQUEST);
                    } else {
                        // validator가 없거나 유효성 검증에 통과할 경우 컨트롤러에게 HttpRequest
                        Function<HttpRequest, HttpResponse> controller = vc.getController();
                        response = controller.apply(httpRequest);
                    }
                } else {
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
