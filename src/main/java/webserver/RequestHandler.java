package webserver;

import java.io.*;
import java.net.Socket;
import java.util.function.BiConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ResponseBuilder;
import util.URLMapper;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String requestLine = reader.readLine();

            StringBuilder sb = new StringBuilder(requestLine).append("\n");

            String line;
            while (!(line = reader.readLine()).isEmpty())
                sb.append(line).append("\n");

            logger.debug("Connection IP : {}, Port : {}, request: {}",
                    connection.getInetAddress(), connection.getPort(), sb);

            String[] requestParts = requestLine.split(" ");
            String requestMethod = requestParts[0];
            String requestPath = requestParts[1];

            BiConsumer<OutputStream, String> handler = URLMapper.getMethod(requestMethod, requestPath);
            if (handler != null) {
                handler.accept(out, requestPath);
            } else {
                ResponseBuilder.responseFile(out, requestPath);
            }
        } catch (IOException e) {
            logger.error("Error processing request: {}", e.getMessage());
        }
    }
}
