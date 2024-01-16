package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

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
            byte[] body = Files.readAllBytes(new File(path).toPath());

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
