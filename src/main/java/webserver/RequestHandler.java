package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String requestline = br.readLine();

            // HTTP REQEUST LINE 출력
            String requestURL = getRequestURL(requestline);
            logger.debug("[REQUEST LINE] {}", requestline);

            // HTTP HEADER LINE 출력
            String headerLine;
            while ((headerLine = br.readLine()) != null && !headerLine.isEmpty()) {
                logger.debug("[Header] {}", headerLine);
            }

            byte[] body;

            if (isHTML(requestURL)) {
                String location = "src/main/resources/templates" + requestURL;
                body = Files.readAllBytes(new File(location).toPath());
            } else {
                String[] tokens = requestURL.split("\\?");
                Map<String, String> params = parse(tokens[1]);

                User user = createUser(params);
                logger.debug("{}", user);

                body = "LOGIN OK".getBytes();
            }

            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static User createUser(Map<String, String> params) {
        String userId = params.get("userId");
        String password = params.get("password");
        String name = params.get("name");
        String email = params.get("email");

        User user = new User(userId, password, name, email);
        return user;
    }

    // html 파일을 요청하는지 확인
    boolean isHTML(String requestURL) {
        if (requestURL.endsWith(".html")) {
            return true;
        }
        return false;
    }

    // Request line에서 url만 추출
    public String getRequestURL(String requestLine) {
        String[] tokens = requestLine.split(" ");
        return tokens[1];
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    // login url parsing 기능
    public static Map<String, String> parse(String query) {
        Map<String, String> params = new HashMap<>();

        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            params.put(keyValue[0], keyValue[1]);
        }

        return params;
    }
}
