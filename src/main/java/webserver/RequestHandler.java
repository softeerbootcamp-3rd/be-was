package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import controller.Controller;
import controller.ControllerMappingMap;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.HttpRequest;
import response.HttpResponse;

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
            HttpRequest request = makeHttpRequest(in);
            HttpResponse response = new HttpResponse();
            Controller controller = ControllerMappingMap.getController(request.getUrl());
            controller.process(request, response);

            DataOutputStream dos = new DataOutputStream(out);
            renderResponse(dos, response);


//            HttpResponse response = new HttpResponse();
//            if (isHTML(request.getUrl())) {
//                String url = "src/main/resources/templates" + request.getUrl();
//                File file = new File(url);
//                if (file.isFile()) {
//                    byte[] body = Files.readAllBytes(new File(url).toPath());
//                    setResponse(response, body);
//                    response.send();
//                } else {
//
//                }
//            }

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

//            response200Header(dos, body.length);
//            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static HttpRequest makeHttpRequest(InputStream in) throws IOException {
        HttpRequest request = new HttpRequest();

        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        String requestLine = br.readLine();
        String[] values = requestLine.split(" ");
        request.setMethod(values[0]);
        request.setUrl(values[1]);
        request.setVersion(values[2]);

        String headerLine;
        while ((headerLine = br.readLine()) != null && !headerLine.isEmpty()) {
            String[] pair = headerLine.split(":");
            if (pair.length == 2) {
                String fieldName = pair[0].trim();
                String value = pair[1].trim();
                request.getHeaders().put(fieldName, value);
            }
        }
        return request;
    }

    private static void renderResponse(DataOutputStream dos, HttpResponse response) {
        try {
            dos.writeBytes(response.getVersion() + " ");
            dos.writeBytes(response.getStatusCode() + " ");
            dos.writeBytes(response.getStatusMessage() + " \r\n");
            Map<String, String> headers = response.getHeaders();
            for (String key : headers.keySet()) {
                dos.writeBytes(key+": ");
                dos.writeBytes(headers.get(key)+"\r\n");
            }
            dos.writeBytes("\r\n");

            dos.write(response.getBody(), 0, response.getBody().length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
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

}
