package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.function.Function;

import http.request.RequestLine;
import http.response.HttpResponse;
import http.request.HttpRequest;
import http.HttpStatus;
import resource.ResourceHandler;
import utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileReader;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final Socket connection;
    private static final String INDEX_PAGE = "/index.html";
    private static final String REDIRECT_PAGE = "/404.html";

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // HttpRequest 처리 로직
            HttpRequest httpRequest = processRequest(in);

            // 동적 & 정적 자원 분할 처리
            String requestUrl = httpRequest.getRequestLine().getUri();
            HttpResponse response;

            // "/" -> "/index.html"로 반환
            if (requestUrl.equals("/")) {
                requestUrl = INDEX_PAGE;
            }

            // 정적 자원부터 탐색 후 동적 자원 처리
            // 정적 자원 처리 - basePath 찾은 경우
            String basePath = FileReader.getBasePath(requestUrl);
            if (!basePath.isEmpty()) {
                response = handleStaticResource(basePath, requestUrl, httpRequest);
            } else {
                // 동적 자원 처리
                response = handleDynamicResource(httpRequest.getRequestLine(), httpRequest);
            }

            // 응답
            DataOutputStream dos = new DataOutputStream(out);
            response.send(dos, httpRequest);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private HttpRequest processRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        // httpRequest 생성 및 출력
        HttpRequest httpRequest = new HttpRequest(br);
        logger.debug(httpRequest.toString());

        return httpRequest;
    }

    private HttpResponse handleStaticResource(String basePath, String requestUrl, HttpRequest httpRequest) {
        String filePath = basePath + Parser.extractPath(requestUrl);
        return ResourceHandler.process(filePath, httpRequest);
    }

    private HttpResponse handleDynamicResource(RequestLine requestLine, HttpRequest httpRequest) {
        ValidatorControllerMapper vc = ValidatorControllerMapper.getValidatorAndControllerByPath(requestLine.getMethodAndPath());

        if (vc != null) {
            Function<String, Boolean> validator = vc.getValidator();
            // 유효성 검증 실패할 경우 BAD_REQUEST 반환
            if (validator != null && !validator.apply(httpRequest.getBody())) {
                return HttpResponse.of(HttpStatus.BAD_REQUEST);
            } else {
                // validator 없거나 유효성 검증에 통과할 경우 컨트롤러에게 HttpRequest 전달
                Function<String, HttpResponse> controller = vc.getController();
                return controller.apply(httpRequest.getBody());
            }
        } else {
            Map<String, String> header = Map.of("Location", REDIRECT_PAGE);
            return HttpResponse.of(HttpStatus.REDIRECT, header);
        }
    }
}
