package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final int BUFFER_SIZE = 4096;
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
            ///
            //BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            //StringTokenizer str = new StringTokenizer(reader.readLine());
            //str.nextToken();

            byte[] body = null;
            String res = readRequest(in);
            logger.debug(res);
            String[] parsing = res.split("\\s+");
            String url = parsing[1];

            //Path currentPath = Paths.get("").toAbsolutePath();
            //boolean fileExists = Files.exists(Paths.get(filePath));

            String filePath = "./src/main/resources/templates/index.html";

            if(url.equals("/index.html")){
                body = Files.readAllBytes(new File(filePath).toPath());
            }else{
                body = "Hello World".getBytes();
            }
            response200Header(dos, body.length);
            responseBody(dos, body);

            ///

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
    ///

    private String readRequest(InputStream inputStream) throws IOException {
        // Read the request headers
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder requestBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            requestBuilder.append(line).append("\r\n");
        }

        // Read the request body if present
        if ("POST".equals(getRequestMethod(requestBuilder.toString()))) {
            int contentLength = getContentLength(requestBuilder.toString());
            char[] buffer = new char[BUFFER_SIZE];
            int bytesRead;
            while (contentLength > 0 && (bytesRead = reader.read(buffer, 0, Math.min(contentLength, BUFFER_SIZE))) != -1) {
                requestBuilder.append(buffer, 0, bytesRead);
                contentLength -= bytesRead;
            }
        }

        return requestBuilder.toString();
    }
    private String getRequestMethod(String request) {
        // Extract the request method from the request headers
        return request.split(" ")[0];
    }


    private int getContentLength(String request) {
        // Extract the Content-Length from the request headers
        String[] headers = request.split("\r\n");
        for (String header : headers) {
            if (header.startsWith("Content-Length:")) {
                return Integer.parseInt(header.split(":")[1].trim());
            }
        }
        return 0;
    }



    ///

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
