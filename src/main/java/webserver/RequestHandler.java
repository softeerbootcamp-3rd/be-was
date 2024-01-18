package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.File;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

import dto.RequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Util;
import view.OutputView;

import static config.WebServerConfig.userController;

public class RequestHandler implements Runnable {
    public static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final String RESOURCES_PATH = "src/main/resources/";
    private static final String INDEX_FILE_PATH = "/index.html";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            RequestDto requestDto = createRequestDto(in);
            OutputView.printRequestDto(requestDto);
            String path = requestDto.getPath();
            String method = requestDto.getMethod();

            URI uri = new URI("http://" + requestDto.getHost() + path);
            if (uri.getQuery() == null) {
                createResponse(out, requestDto.getPath());
            }
            if (method.equals("GET") && path.startsWith("/user/create")) {
                userController.create(uri);
                redirect(out);
            }
        } catch (IOException | URISyntaxException | IllegalAccessException e) {
            logger.error(e.getMessage());
        }
    }

    private static RequestDto createRequestDto(InputStream in) throws IOException {
        RequestDto requestDto = new RequestDto();

        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = br.readLine();

        String[] requestLine = Util.splitRequestLine(line);
        requestDto.setMethodAndPath(requestLine[0], requestLine[1]);

        Map<RequestHeader, String> requestHeaders = new HashMap<>();
        while (!line.equals("")) {
            line = br.readLine();
            String[] requestHeader = Util.splitRequestHeader(line);
            RequestHeader property = RequestHeader.findProperty(requestHeader[0]);
            if (property != RequestHeader.NONE) {
                requestHeaders.put(property, requestHeader[1]);
            }
        }
        requestDto.setRequestHeaders(requestHeaders);
        return requestDto;
    }

    private void createResponse(OutputStream out, String url) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        byte[] body = Files.readAllBytes(new File(getFilePath(url) + url).toPath());
        response200Header(dos, body.length);
        responseBody(dos, body);
    }

    private void redirect(OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        response302Header(dos, INDEX_FILE_PATH);
    }

    private static String getFilePath(String url) {
        String path = RESOURCES_PATH;
        if (url.startsWith("/css") || url.startsWith("/fonts") || url.startsWith("/images") || url.startsWith("/js") || url.equals("/favicon.ico")) {
            path += "static";
        } else {
            path += "templates";
        }
        return path;
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

    private void response302Header(DataOutputStream dos, String location) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + location);
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
