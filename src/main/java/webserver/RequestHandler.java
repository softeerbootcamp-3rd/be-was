package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
            BufferedReader inBufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            List<String> header = getHeaderRequestInfo(inBufferedReader);
            Optional<String> path = getPath(header);
            if(path.isPresent() && path.get().equals("/index.html")){
                byte[] body = Files.readAllBytes(new File("src/main/resources/templates" + path.get()).toPath());
                DataOutputStream dos = new DataOutputStream(out);
                response200Header(dos, body.length);
                responseBody(dos, body);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private Optional<String> getPath(List<String> header) {
        if(header.isEmpty()){
            return Optional.empty();
        }
        return Optional.ofNullable(header.get(0).split(" ")[1]);
    }

    private List<String> getHeaderRequestInfo(BufferedReader inBufferedReader) throws IOException {
        List<String> header = new ArrayList<>();
        String temp;
        if(inBufferedReader.ready()){
            while (!(temp = inBufferedReader.readLine()).isEmpty()){
                header.add(temp);
            }
        }
        header.forEach(logger::debug);
        logger.debug("----------------------구분선----------------------");
        return header;
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
