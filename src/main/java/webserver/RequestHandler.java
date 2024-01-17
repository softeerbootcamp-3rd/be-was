package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import db.Database;
import model.HttpRequest;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            // httpRequest 객체 생성
            HttpRequest httpRequest = getHttpRequest(in);

            if (httpRequest == null) {
                return;
            }

            // httpRequest 출력
            logger.debug(httpRequest.toString());

            String requestURL = httpRequest.getTarget();

            DataOutputStream dos = new DataOutputStream(out);
            // 파라미터 처리
            if (requestURL.contains("?")) {
                requestURL = runAPI(requestURL);
                linkPage(requestURL, dos);
            } else {
                // 페이지 링크
                linkPage(requestURL, dos);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private String runAPI(String requestURL) {
        String path = requestURL.split("\\?")[0];
        String params = requestURL.split("\\?")[1];

        // 회원가입
        if (path.equals("/user/create")) {
            String[] tokens = params.split("&");
            Map<String, String> map = new HashMap<>();
            for (String token : tokens) {
                map.put(token.split("=")[0], token.split("=")[1]);
            }
            createUser(map);
            path = "/user/form.html";
        }

        return path;
    }

    private void createUser(Map<String, String> param) {
        User user = new User(param.get("userId"), param.get("password"), param.get("name"), param.get("email"));

        if (Database.findUserById(user.getUserId()) == null) {
            Database.addUser(user);
        }

        logger.info(Database.findAll().toString());
    }

    private void linkPage(String requestURL, DataOutputStream dos) throws IOException {
        String basePath = getBasePath(requestURL);

        byte[] body;
        if (basePath.isEmpty()) {
            body = "Hello World".getBytes();
        } else {
            body = Files.readAllBytes(new File(basePath + requestURL).toPath());
        }

        String content = getContent(requestURL);
        response200Header(dos, body.length, content);
        responseBody(dos, body);

    }

    private String getContent(String url) {
        if (url.contains(".html")) {
            return "text/html";
        }

        if (url.contains(".css")) {
            return "text/css";
        }

        if (url.contains(".js")) {
            return "application/x-javascript";
        }

        if (url.contains(".png")) {
            return "image/png";
        }

        if (url.contains(".ico")) {
            return "image/x-icon";
        }

        return "text/html";
    }

    private String getBasePath(String requestURL) {
        if (requestURL.contains(".html")) {
            return "src/main/resources/templates";
        }

        if(requestURL.contains("/css/") || requestURL.contains("/fonts/") || requestURL.contains("/images/") || requestURL.contains("/js/") || requestURL.contains("favicon.ico"))
            return "src/main/resources/static";

        return "";
    }

    private HttpRequest getHttpRequest(InputStream in) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

        // http request 처리

        // parser -> startline parser, httprequest form, queryparser
        String line;
        if (!(line = bufferedReader.readLine()).isEmpty()) {
            // start line 처리
            String[] tokens = line.split(" ");

            HttpRequest httpRequest = new HttpRequest(tokens[0], tokens[1], tokens[2]);

            // header 처리
            while (!(line = bufferedReader.readLine()).isEmpty()) {
                tokens = line.split(": ");
                httpRequest.updateHeaders(tokens[0], tokens[1]);
            }

            return httpRequest;
        }
        return null;
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String content) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + content + ";charset=utf-8\r\n");
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
}