package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Objects;
import db.Database;
import model.RequestHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.*;

public class RequestHandler implements Runnable {
    private Socket connection;
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            RequestHeader requestHeader = new RequestHeader();
            Database database = new Database();

            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            ParsingService parsingService = new ParsingService(br, requestHeader);
            String path = requestHeader.getPath();
            String method = requestHeader.getHttpMethod();
            logger.debug(">>  {} >> {}", method, path);

            HashMap<String, HTTPMethod> httpMethods = initializeHttpMethods();

            if (Objects.equals(method, "GET")) {
                //회원가입처리
                if (path.contains("/create")) {
                    CreateUserService createUserService = new CreateUserService(database, path);
                    path = createUserService.getUrl();
                    requestHeader.setPath(path);
                }

                // 리소스 처리
                ResourceService resourceService = new ResourceService();
                DataOutputStream dos = new DataOutputStream(out);
                String fileExtension = resourceService.separatedFileExtension(requestHeader);
                String detailPath = resourceService.getDetailPath(fileExtension,path);
                String contentType = resourceService.getContextType(fileExtension);

                // 다양한 상태 코드 처리해야할 때 변경
                if (detailPath.equals("404")) {
                    String errorPageContent = "<html><head><title>404 Not Found</title></head><body><h1>404 Not Found</h1></body></html>";
                    byte[] body = errorPageContent.getBytes(StandardCharsets.UTF_8);
                    response400Header(dos, body.length);
                    responseBody(dos, body);
                } else {
                    // get.body();
                    // get.dos();
                    byte[] body = Files.readAllBytes(new File(detailPath).toPath());
                    response200Header(dos, body.length, contentType);
                    responseBody(dos, body);
                }

        } else if (Objects.equals(method, "POST")) {
                logger.debug("POST");
            }

        }
        catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static HashMap<String, HTTPMethod> initializeHttpMethods() {
        HashMap<String, HTTPMethod> actions = new HashMap<>();
        actions.put("GET", new GetService());
        actions.put("POST", new PostService());
        return actions;
    }

    private void response400Header(DataOutputStream dos, int lengthOfBodyContent) throws IOException {
        dos.writeBytes("HTTP/1.1 404 Not Found\r\n");
        dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String type) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: "+type+"\r\n");
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
