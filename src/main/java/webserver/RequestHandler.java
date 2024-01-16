package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.File;
import java.net.Socket;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

import dto.RequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Util;
import view.OutputView;

public class RequestHandler implements Runnable {
    public static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            RequestDto requestDto = createRequestDto(in);
            String url = requestDto.getUrl();
            OutputView.printRequestDto(requestDto);

            DataOutputStream dos = new DataOutputStream(out);

            String path = "src/main/resources/";
            if (url.startsWith("/css") || url.startsWith("/fonts") || url.startsWith("/images") || url.startsWith("/js") || url.equals("/favicon.ico")) {
                path += "static";
            } else {
                path += "templates";
            }

            byte[] body = Files.readAllBytes(new File(path + url).toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static RequestDto createRequestDto(InputStream in) throws IOException {
        RequestDto requestDto = new RequestDto();

        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = br.readLine();

        String[] requestLine = Util.splitRequestLine(line);
        requestDto.setMethodAndURL(requestLine[0], requestLine[1]);

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
