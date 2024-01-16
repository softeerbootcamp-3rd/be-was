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
import model.User;
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
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            Request request = getRequest(in);
            byte[] body = handleRequest(request);
            extracted(out, body, request);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid request: {}", e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
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

    private byte[] handleRequest(Request request) throws IOException {
        String filePath = request.getFilePath();
        System.out.println("filePath : " + filePath);
        if (request.getUrl().startsWith(USER_PATH)) {
            String remainingPath = request.getUrl().substring(USER_PATH.length());
            handleUser(remainingPath);
        }

        // url 에서 예외 처리 되는 부분 생각
        File file = new File(filePath);
        if (!file.exists()) {
            return "404 File Not Found".getBytes();
        }

        return Files.readAllBytes(file.toPath());
    }

    private void handleUser(String path) throws UnsupportedEncodingException {
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
            }
        }
    }

    private Map<String, String> parseQueryString(String queryString) throws UnsupportedEncodingException {
        Map<String, String> queryParams = new HashMap<>();
        for (String param : queryString.split("&")) {
            String[] keyValue = param.split("=");
            if (keyValue.length > 1) {
                String key = keyValue[0];
                String value = URLDecoder.decode(keyValue[1], "UTF-8");
                queryParams.put(key, value);
            }
        }
        return queryParams;
    }
    private void extracted(OutputStream out, byte[] body, Request request) {
        DataOutputStream dos = new DataOutputStream(out);
        response200Header(dos, body.length, request);
        responseBody(dos, body);
    }


    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, Request request) {
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

    private String getContentType(String filePath) {
        String extension = filePath.substring(filePath.lastIndexOf(".") + 1);
        return MimeType.getContentType(extension);
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}