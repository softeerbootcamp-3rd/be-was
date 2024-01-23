package webserver;

import java.io.*;
import java.lang.reflect.Method;
import java.net.Socket;

import constant.HttpStatus;
import constant.MimeType;
import exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.*;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        InputStream in = null;
        OutputStream out = null;
        try { in = connection.getInputStream(); } catch (IOException e) { logger.error(e.getMessage()); }
        try { out = connection.getOutputStream(); } catch (IOException e) { logger.error(e.getMessage()); }
        if (in == null || out == null) {
            closeConnections(in, out);
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            HttpRequest request = new HttpRequest(reader);

            logger.debug("Connection IP : {}, Port : {}, request: {}",
                    connection.getInetAddress(), connection.getPort(), request.getPath());

            Method handler = RequestMapper.getMethod(request);
            if (handler != null) {
                RequestMapper.invoke(handler, request).send(out, logger);
            } else if (request.getMethod().equals("GET")) {
                byte[] fileContent = ResourceLoader.getFileContent(request.getPath());
                // html 파일이면 동적으로 내용 변경
                if (MimeType.HTML.getMimeType().equals(ResourceLoader.getMimeType(request.getPath())))
                    fileContent = HtmlBuilder.process(request, fileContent);

                HttpResponse.builder()
                        .status(HttpStatus.OK)
                        .addHeader("Content-Type", ResourceLoader.getMimeType(request.getPath()))
                        .body(fileContent)
                        .build()
                        .send(out, logger);
            } else
                throw new ResourceNotFoundException(request.getPath());

        } catch (ResourceNotFoundException e) {
            HttpResponse.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .addHeader("Content-Type", "text/plain")
                    .body(HttpStatus.NOT_FOUND.getFullMessage())
                    .build()
                    .send(out, logger);

        } catch (IllegalStateException | IOException e) {
            logger.error("error processing request: {}", e.getMessage());
            HttpResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage())
                    .build()
                    .send(out, logger);
        }
        closeConnections(in, out);
    }

    void closeConnections(InputStream in, OutputStream out) {
        try {
            if (in != null)
                in.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        try {
            if (out != null)
                out.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
