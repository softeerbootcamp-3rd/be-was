package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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


            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();

            //line: request line GET /index.html HTTP/1.1
            // 위와 같은 형태이므로 여기서 URL만 추출
            String[] tokens = line.split(" ");
            String url = tokens[1];


            // debug 라인 분석
            // 첫줄 - 메소드, url, http버전
            // Host - 서버 주소
            // Connection: keep-alive -> TCP에서 배운 keep-alive
            // Cahche-Control: max-age=0 -> 유효기간, 0이된 후 요청하면 새로 요청을 보내지만 그 전에는 메모리에서 가지고 옴
            // 나머지는 클라이언트의 환경 및 스펙

            logger.debug("request line {}",line);
            while(line != null && !line.isEmpty()) {
                line = br.readLine();
                logger.debug("header : {}", line);
            }
            logger.debug("Ending Connected IP : {}, Port : {}", connection.getInetAddress(),
                    connection.getPort());
            System.out.println("================================================================");
            DataOutputStream dos = new DataOutputStream(out);


            // 1. File이라는 객체를 templateFilePath + url로 접근
            // 2. File객체를 Path객체로 변환

            String templateFilePath = "be-was/src/main/resources/templates";
            Path path = new File(templateFilePath + url).toPath();
            byte[] body = Files.readAllBytes(path);


            //byte[] body = "Hello 동민".getBytes();
            response200Header(dos, body.length);
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
            logger.debug(dos.toString());
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
