package webserver;

import java.io.*;
import java.net.Socket;

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
            byte[] body = "Hello World".getBytes();

            HttpRequestInformation request = getHttpRequest(in);
            printHttpRequestInformation(request);

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

    private HttpRequestInformation getHttpRequest(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));

        HttpRequestInformation httpRequestInformation = new HttpRequestInformation();

        String line = br.readLine();
        String[] lines = line.split(" ");
        httpRequestInformation.setMethod(lines[0]);
        httpRequestInformation.setUrl(lines[1]);
        httpRequestInformation.setHttpVersion(lines[2]);

        while (!line.equals("")) {
            line=br.readLine();
            if (line.startsWith("Accept:")) {
                httpRequestInformation.setAccept(line);
            } else if (line.startsWith("Connection")) {
                httpRequestInformation.setConnection(line);
            } else if (line.startsWith("Host:")) {
                httpRequestInformation.setHost(line);
            }
        }

        return httpRequestInformation;
    }

    private void printHttpRequestInformation(HttpRequestInformation requestInformation) {
        logger.debug("= = HTTP REQUEST INFORMATION = =");
        logger.debug(requestInformation.getMethod() + " " + requestInformation.getUrl() + " " + requestInformation.getHttpVersion());
        logger.debug(requestInformation.getHost());
        logger.debug(requestInformation.getConnection());
        logger.debug(requestInformation.getAccept());
    }
}
