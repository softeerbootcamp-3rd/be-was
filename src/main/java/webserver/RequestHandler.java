package webserver;

import java.io.*;
import java.net.Socket;
<<<<<<< Updated upstream
import java.nio.file.Files;
=======
import java.util.function.Function;
>>>>>>> Stashed changes

import dto.RequestDto;
import dto.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
<<<<<<< Updated upstream
import util.ResourceLoader;
=======
import util.ControllerMapper;
import util.HttpStatus;
import util.RequestBuilder;
import util.ResponseBuilder;
>>>>>>> Stashed changes

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

<<<<<<< Updated upstream
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(inputStreamReader);

            String path = br.readLine();
            logger.debug(path);
            path = path.split(" ")[1];
            StringBuilder sb = new StringBuilder(path);
            String line;
            while (!(line = br.readLine()).isEmpty()) {
                logger.debug(line);
            }
            System.out.println();

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = {};

            String filePath = "src/main/resources/templates";
            String contentType = "text/html;charset=utf-8";
            if (path.equals("/")) {
                body = "Hello World".getBytes();
            } else {
                if (path.startsWith("/css") || path.startsWith("/fonts") || path.startsWith("/images") || path.startsWith("/js")) {
                    contentType = ResourceLoader.getContentType(path);
                    filePath = "src/main/resources/static";
                }

                body = Files.readAllBytes(new File(filePath + path).toPath());
=======
            RequestBuilder requestBuilder = new RequestBuilder(connection, in);
            logger.debug(requestBuilder.toString());
            DataOutputStream dos = new DataOutputStream(out);

            // GET만 다루고 있으므로 일단 body는 null로
            RequestDto requestDto = new RequestDto<>(requestBuilder.getUri(), null);

            Function<RequestDto, ResponseDto> controller =
                    ControllerMapper.getController(requestBuilder.getHttpMethod());

            if (controller != null) {
                ResponseDto responseDto = controller.apply(requestDto);
                ResponseBuilder.response(dos, responseDto);

            } else {
                ResponseBuilder.response(dos, HttpStatus.INTERNAL_SERVER_ERROR);
>>>>>>> Stashed changes
            }

            response200Header(dos, body.length, contentType);
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

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + "\r\n");
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
