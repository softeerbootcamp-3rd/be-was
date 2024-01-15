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
            logger.debug("request: {}", line);
            // header 전부 출력
            while(!line.equals("")) {
                line = br.readLine();
                logger.debug("header : {}", line);
            }
            // 해당 파일을 읽고 응답
            String path = "./src/main/resources";
            String type = "html";
            // 1. html일 경우
            if(tokens[1].contains(".html")) {
                path += "/templates" + tokens[1];
            }
            // 2. css일 경우
            else if(tokens[1].contains(".css")) {
                path += "/static" + tokens[1];
                type = "css";
            }
            // 3. fonts일 경우
            else if(tokens[1].contains("halflings-regular")) {
                path += "/static" + tokens[1];
                type = "fonts";
            }
            // 4. images일 경우
            else if(tokens[1].contains(".png")) {
                path += "/static" + tokens[1];
                type = "images";
            }
            // 5. js일 경우
            else if(tokens[1].contains(".js")) {
                path += "/static" + tokens[1];
                type = "js";
            }
            // 6. .ico일 경우
            else {
                path += "/static" + tokens[1];
                type = "ico";
            }

            logger.debug("path: {}", path);
            byte[] body = Files.readAllBytes(new File(path).toPath());

            response200Header(dos, body.length, type);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String type) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            if(type.equals("html"))
                dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            else if(type.equals("css"))
                dos.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
            else if(type.equals("fonts"))
                dos.writeBytes("Content-Type: */*;charset=utf-8\r\n");
            else if(type.equals("images"))
                dos.writeBytes("Content-Type: */*;charset=utf-8\r\n");
            else if(type.equals("js"))
                dos.writeBytes("Content-Type: */*;charset=utf-8\r\n");
            else
                dos.writeBytes("Content-Type: image/avif;charset=utf-8\r\n");
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
