package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.http.*;
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
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader inBufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            HttpRequest header = getRequestHeader(inBufferedReader);
            logger.debug(header.toString());
            byte[] body = Files.readAllBytes(getFilePath(header));
            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static Path getFilePath(HttpRequest header) {
        String filePath = header.getStartLine().getPathUrl();
        if (filePath.contains("html")) {
            return new File("src/main/resources/templates" + filePath).toPath();
        }
        return new File("src/main/resources/static" + filePath).toPath();

    }

    private HttpRequest getRequestHeader(BufferedReader inBufferedReader) throws IOException {
        List<String> httpRequest = new ArrayList<>();
        String temp;
        while (!(temp = inBufferedReader.readLine()).isEmpty()){
            httpRequest.add(temp);
        }
        StartLine startLine = parseStartLine(httpRequest);
        RequestHeaders requestHeaders = parseRequestHeaders(httpRequest);
        Body body = parseRequestBody(httpRequest);
        return new HttpRequest(startLine, requestHeaders, body);
    }

    private Body parseRequestBody(List<String> httpRequest) {
        return null;
    }

    private RequestHeaders parseRequestHeaders(List<String> httpRequest) {
        HashMap<String, String> header = new HashMap<>();
        for (int i=1; i<httpRequest.size(); i++) {
            String[] strings = httpRequest.get(i).split(": ");
            header.put(strings[0], strings[1]);
        }
        String host = header.get("Host");
        String userAgent = header.get("User-Agent");
        String accept = header.get("Accept");
        header.remove("Host");
        header.remove("User-Agent");
        header.remove("Accept");
        return new RequestHeaders(host, userAgent, accept, header);
    }

    private StartLine parseStartLine(List<String> content) {
        String[] startLine = content.get(0).split(" ");
        return new StartLine(HttpMethod.valueOf(startLine[0]),startLine[1], startLine[2]);
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
