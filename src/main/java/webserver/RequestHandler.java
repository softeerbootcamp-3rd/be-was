package webserver;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

            HttpRequest request = new HttpRequest();

            setRequestLine(br, request);
            setHeaders(br, request);

            HttpResponse response = new HttpResponse(out);
            if (isHTML(request.getUrl())) {
                String url = "src/main/resources/templates" + request.getUrl();
                File file = new File(url);
                if (file.isFile()) {
                    byte[] body = Files.readAllBytes(new File(url).toPath());
                    setResponse(response, body);
                    response.send();
                }
            }

//            byte[] body;
//
//            if (isHTML(request.getUrl())) {
//                String location = "src/main/resources/templates" + request.getUrl();
//                File file = new File(location);
//                if (file.isFile()) {
//                    HttpResponse response = new HttpResponse(out);
//                }
//                body = Files.readAllBytes(new File(location).toPath());
//            } else {
//                String[] tokens = requestURL.split("\\?");
//                Map<String, String> params = parse(tokens[1]);
//
//                User user = createUser(params);
//                logger.debug("{}", user);
//
//                body = "LOGIN OK".getBytes();
//            }
//
//            DataOutputStream dos = new DataOutputStream(out);
//            response200Header(dos, body.length);
//            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static void setResponse(HttpResponse response, byte[] body) {
        response.setVersion("HTTP/1.1");
        response.setStatusCode("200");
        response.setStatusMessage("OK");
        response.setBody(body);
    }

    private static void setRequestLine(BufferedReader br, HttpRequest request) throws IOException {
        String requestLine = br.readLine();
        String[] values = requestLine.split(" ");
        request.setMethod(values[0]);
        request.setUrl(values[1]);
        request.setVersion(values[2]);
    }

    private static void setHeaders(BufferedReader br, HttpRequest request) throws IOException {
        String headerLine;
        while ((headerLine = br.readLine()) != null && !headerLine.isEmpty()) {
//                logger.debug("[Header] {}", headerLine);
            String[] pair = headerLine.split(":");
            if (pair.length == 2) {
                String fieldName = pair[0].trim();
                String value = pair[1].trim();
                request.getHeaders().put(fieldName, value);
            }
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
