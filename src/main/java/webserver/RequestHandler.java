package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import dto.UserCreateRequestDto;
import model.MimeType;
import model.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final String USER_PATH = "/user";
    private static final UserService userService = new UserService();

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            DataOutputStream dos = new DataOutputStream(out); // dos 초기화
            Request request = getRequest(in);
            byte[] body = handleRequest(request, dos);
            if (body.length > 0) {
                sendResponse(body, request, dos); // 응답 보내기
            }
        } catch (IllegalArgumentException | IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void sendResponse(byte[] body, Request request, DataOutputStream dos) {
        response200Header(body.length, request, dos); // 수정된 메서드 호출
        responseBody(body, dos);
    }
    private static Request getRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

        String line = br.readLine();
        Request request = new Request(line);
        logger.debug("request : {}", request.toString());

        while (!line.isEmpty()) {
            line = br.readLine();
            request.addHeader(line);
        }
        return request;
    }

    private byte[] handleRequest(Request request, DataOutputStream dos) throws IOException {
        // 요청인 경우
        String url = request.getUrl();
        if (request.getUrl().startsWith(USER_PATH)) {
            String remainingPath = request.getUrl().substring(USER_PATH.length());
            if (url.startsWith(USER_PATH + "/create")) {
                return handleUser(remainingPath, dos);
            }
        }

        // 정적 리소스 처리
        return serveStaticResource(request);
    }

    private byte[] serveStaticResource(Request request) throws IOException {
        String filePath = request.getFilePath();
        File file = new File(filePath);
        if (!file.exists()) {
            return "404 File Not Found".getBytes();
        }
        return Files.readAllBytes(file.toPath());
    }

    private byte[] handleUser(String path, DataOutputStream dos) throws UnsupportedEncodingException {
        System.out.println("path : " + path);
        if (path.startsWith("/create")) {
            logger.debug("등록 !!!!! ");
            String queryString = path.substring("/create".length());
            if (queryString.startsWith("?")) {
                queryString = queryString.substring(1);
                Map<String, String> queryParams = parseQueryString(queryString);

                UserCreateRequestDto userCreateRequestDto =
                        new UserCreateRequestDto(queryParams.get("userId"),
                                                queryParams.get("password"),
                                URLDecoder.decode(queryParams.get("name"), StandardCharsets.UTF_8),
                                queryParams.get("email"));

                // DTO 사용
                userService.create(userCreateRequestDto);
                responseRedirectHeader(dos, "/index.html");
                return new byte[0];
            }
        }
        responseRedirectHeader(dos, "/index.html");
        return new byte[0];
    }

    private void responseRedirectHeader(DataOutputStream dos, String redirectUrl) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found\r\n");
            dos.writeBytes("Location: " + redirectUrl + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private Map<String, String> parseQueryString(String queryString) throws UnsupportedEncodingException {
        Map<String, String> queryParams = new HashMap<>();
        for (String param : queryString.split("&")) {
            String[] keyValue = param.split("=");
            if (keyValue.length > 1) {
                String key = keyValue[0];
                String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                queryParams.put(key, value);
            }
        }
        return queryParams;
    }

    private void response200Header(int lengthOfBodyContent, Request request, DataOutputStream dos) {
        try {
            String contentType = getContentType(request.getFilePath());
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(byte[] body, DataOutputStream dos) {
        try {
            dos.write(body, 0, body.length);
            dos.flush(); // 스트림을 플러시
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private String getContentType(String filePath) {
        String extension = filePath.substring(filePath.lastIndexOf(".") + 1);
        return MimeType.getContentType(extension);
    }

}