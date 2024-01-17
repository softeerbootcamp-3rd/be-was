package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;

import db.Database;
import model.RequestHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.CreateUserService;
import service.FileService;
import service.ParsingService;

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
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            RequestHeader requestHeader = new RequestHeader();
            ParsingService parsingService = new ParsingService(br, requestHeader);
            Database database = new Database();

            /*
            // 특정 유용한 request header만 출력
            logger.debug("====useful RequestHeader====");
            logger.debug("{} ", requestHeader.getGeneralHeader());
            logger.debug("{} ", requestHeader.getAccpet());
            logger.debug("{} ", requestHeader.getAccept_encoding());
            logger.debug("{} ", requestHeader.getAccept_language());
            logger.debug("{} ", requestHeader.getUpgrade_insecure_requests());
            logger.debug("===========================");
            */

            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            // 리소스 분석하여 파싱
            String path = requestHeader.getPath();
            String method = requestHeader.getHTTP_method();
            logger.debug(">>>  {}", requestHeader.getGeneralHeader());
            logger.debug(">>  {} ", path);
            logger.debug(">>  {} ", method);

            if (Objects.equals(method, "GET")) {
                //회원가입처리
                if (path.contains("/create")) {
                    CreateUserService createUserService = new CreateUserService(database, path);
                    path = createUserService.getUrl();
                }

                // 파일 처리
                FileService fileService = new FileService();
                DataOutputStream dos = new DataOutputStream(out);
                String[] parts = path.split("\\.");
                String file_extension = parts[parts.length-1];

                String detail_path = fileService.getPath(file_extension,path);
                String context_type = fileService.getContextType(file_extension);

                logger.debug("~~~~~~~~ {} / {}",detail_path,context_type);

                // 다양한 상태 코드 처리해야할 때 변경
                if (detail_path.equals("404")) {
                    String errorPageContent = "<html><head><title>404 Not Found</title></head><body><h1>404 Not Found</h1></body></html>";
                    byte[] body = errorPageContent.getBytes(StandardCharsets.UTF_8);

                    dos.writeBytes("HTTP/1.1 404 Not Found\r\n");
                    dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
                    dos.writeBytes("Content-Length: " + body.length + "\r\n");
                    dos.writeBytes("\r\n");
                    dos.write(body, 0, body.length);
                    dos.flush();
                } else {
                    byte[] body = Files.readAllBytes(new File(detail_path).toPath());
                    response200Header(dos, body.length, context_type);
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
