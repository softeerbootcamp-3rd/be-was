package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import db.Database;
import model.RequestHeader;
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
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            RequestHeader requestHeader = new RequestHeader();
            ParsingService parsingService = new ParsingService(br,requestHeader);
            Database database = new Database();


            // 특정 유용한 request header만 출력
            logger.debug("====useful RequestHeader====");
            logger.debug("{} ", requestHeader.getGeneralHeader());
            logger.debug("{} ", requestHeader.getAccpet());
            logger.debug("{} ", requestHeader.getAccept_encoding());
            logger.debug("{} ", requestHeader.getAccept_language());
            logger.debug("{} ", requestHeader.getUpgrade_insecure_requests());
            logger.debug("===========================");

            // URL처리
            String path = requestHeader.getPath();
            String HTTP_method = requestHeader.getHTTP_method();

            logger.debug("------>  {} ", requestHeader.getPath());
            logger.debug("------>  {} ", requestHeader.getHTTP_method());

            //회원가입처리
            if(path.contains("/create")){
                CreateUserService createUserService = new CreateUserService(path);

                logger.debug(createUserService.user.toString());

                database.addUser(createUserService.user);

                // 메인페이지로 이어지도록 설정
                path = "/index.html";
            }

            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = Files.readAllBytes(new File("./src/main/resources/templates" + path).toPath());

            //응답헤더
            response200Header(dos, body.length);

            //본문
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
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

/*

        if (path.endsWith(".html")) {
byte[] body = Files.readAllBytes(new File("./src/main/resources/templates" + path).toPath());
response200Header(dos, body.length, "text/html;charset=utf-8");
responseBody(dos, body);
        }
                // CSS 파일인 경우
                else if (path.endsWith(".css")) {
byte[] body = Files.readAllBytes(new File("./src/main/resources/css" + path).toPath());
response200Header(dos, body.length, "text/css;charset=utf-8");
responseBody(dos, body);
        }
                // 다른 파일 형식 처리도 추가 가능
                else {
                // 기타 파일 형식에 대한 처리
                dos.writeBytes("HTTP/1.1 404 Not Found\r\n");
            dos.flush();
        }
                */