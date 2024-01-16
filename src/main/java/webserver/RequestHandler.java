package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

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
            DataOutputStream dos = new DataOutputStream(out);
            // 헤더 값 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            // 요청 라인 읽어오기
            String line = br.readLine();
            if(line == null)
                return;
            // 요청 URL 추출
            String[] tokens = line.split(" ");
            String url = tokens[1];

            String accept = "";
            logger.debug("HTTP Method: {}, Request Target: {}", tokens[0], url);
            // host, accept 출력
            while(!line.equals("")) {
                line = br.readLine();
                if(line.contains("Host"))
                    logger.debug(line);
                else if(line.contains("Accept:")) {
                    // Accept 추출
                    accept = line.substring("Accept: ".length());
                    if(line.contains(","))
                        accept = accept.substring(0, accept.indexOf(","));
                    logger.debug("Accept: {}", accept);
                }
            }

            byte[] body = new byte[10];     // 초기화
            // GET으로 회원가입 기능 구현
            if(url.contains("/user/create?")) {
                tokens = url.split("\\?");
                String[] parameters = tokens[1].split("&");
                String userId = parameters[0].substring(parameters[0].indexOf("=")+1);
                String password = parameters[1].substring(parameters[1].indexOf("=")+1);
                String name = parameters[2].substring(parameters[2].indexOf("=")+1);
                String email = parameters[3].substring(parameters[3].indexOf("=")+1);
                email = email.replace("%40", "@");
                // User 객체에 저장
                User user = new User(userId, password, name, email);
                logger.debug(user.toString());
                String welcome = "Hello, " + user.getName() + "!";      // 회원가입 성공 시 뜨는 문구
                body = welcome.getBytes();
            }
            else {
                // 해당 파일을 읽고 응답
                String path = "./src/main/resources";
                // 1. html일 경우
                if(url.contains(".html")) {
                    path += "/templates" + url;
                }
                // 2. css, fonts, images, js, ico일 경우
                else {
                    path += "/static" + url;
                }

                logger.debug("path: {}", path);
                body = Files.readAllBytes(new File(path).toPath());
            }
            response200Header(dos, body.length, accept);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String accept) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + accept + ";charset=utf-8\r\n");
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
