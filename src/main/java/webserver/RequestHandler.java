package webserver;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static utils.ParsingUtils.combineResources;

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

            //Request header 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            String line = br.readLine();
            logger.debug("request line : {}", line);

            ArrayList<String> url = new ArrayList<>();
            url.add(line.split(" ")[1]);

            //url 읽어오기
            while(!line.equals("")){
                line = br.readLine();

                if (line == null) break;

                if (line.equals("GET")) {
                    String[] tokens = line.split(" ");

                    url.add(tokens[1]);
                }

                //header 정보 출력
                logger.debug("header : {}", line);
            }

            //응답
            byte[] body = combineResources(url);

            String contentType = "text/html";
            if (!url.isEmpty() && url.get(0).endsWith(".css")) {
                contentType = "text/css";
            }
            else if (!url.isEmpty() && url.get(0).endsWith(".js")) {
                contentType = "application/javascript";
            }

            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, body.length, contentType);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }


    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
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
