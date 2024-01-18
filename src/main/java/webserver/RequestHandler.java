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

import dto.RequestHeaderDto;
import dto.RequestLineDto;
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
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            RequestLineDto requestLineDto = RequestParser.parseRequestLine(br);
            RequestHeaderDto requestHeaderDto = RequestParser.parseRequestHeader(br);
            OutputView.printRequest(requestLineDto, requestHeaderDto);

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

    private void createResponse(OutputStream out, String url) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        byte[] body = Files.readAllBytes(new File(getFilePath(url)).toPath());
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
        return path + url;
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
