package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

import controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.RequestUrl;

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
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String line = br.readLine();
            RequestUrl url = RequestUrl.of(line);

            /**
             * HTTP Request Header 로깅
             */
            logger.debug(line);
            while (!(line = br.readLine()).isEmpty()) {
                String[] tokens = line.split(" ");
                if (tokens[0].equals("Host:") || tokens[0].equals("Connection:") || tokens[0].equals("Accept:"))
                    logger.debug(line);
            }

            /**
             * make response
             */
            byte[] body = makeResponseBody(url);
            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private byte[] makeResponseBody(RequestUrl url) throws IOException {
        String path = url.getPath();
        byte[] body;

        if (path.startsWith("/user")) {
            UserController userController = new UserController(url);
            body = userController.run();
        } else {
            if (path.equals("/"))
                path = "/index.html";
            body = Files.readAllBytes(new File("./src/main/resources/templates" + path).toPath());
        }
        return body;
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
