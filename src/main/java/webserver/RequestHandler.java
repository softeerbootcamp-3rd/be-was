package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.StringTokenizer;

import model.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = getHttpRequest(in);

            if (httpRequest != null) {
                // httpRequest 출력
                logger.debug(httpRequest.toString());

                // 페이지 연결
                DataOutputStream dos = new DataOutputStream(out);

                String requestURL = httpRequest.getTarget();
                String basePath = getBasePath(requestURL);

                if (basePath.isEmpty()) {
                    byte[] body = "Hello World".getBytes();
                    response200Header(dos, body.length);
                    responseBody(dos, body);
                } else{
                    byte[] body = Files.readAllBytes(new File(basePath + requestURL).toPath());
                    response200Header(dos, body.length);
                    responseBody(dos, body);
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private String getBasePath(String requestURL) {
        if (requestURL.contains("html")) {
            return "src/main/resources/templates";
        }

        if(requestURL.contains("css") || requestURL.contains("fonts") || requestURL.contains("images") || requestURL.contains("js"))
            return "src/main/resources/static";

        return "";
    }

    private HttpRequest getHttpRequest(InputStream in) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

        // http request 처리
        String line;
        if (!(line = bufferedReader.readLine()).isEmpty()) {
            // start line 처리
            String[] tokens = line.split(" ");

            HttpRequest httpRequest = new HttpRequest(tokens[0], tokens[1], tokens[2]);

            // header 처리
            while (!(line = bufferedReader.readLine()).isEmpty()) {
                tokens = line.split(": ");
                httpRequest.updateHeaders(tokens[0], tokens[1]);
            }

            return httpRequest;
        }
        return null;
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