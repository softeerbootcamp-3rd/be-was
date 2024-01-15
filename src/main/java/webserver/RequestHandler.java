package webserver;

import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final URL RESOURCES_URL = this.getClass().getClassLoader().getResource("./templates");
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

            String uri = parseUri(in);

            setResponse(dos, uri);
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

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void setResponse(DataOutputStream dos, String uri) throws IOException {
        try{
            byte[] body = Files.readAllBytes(new File(RESOURCES_URL.getPath() + uri).toPath());

            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e){
            logger.error(e.getMessage());
        }
    }

    private String parseUri(InputStream in) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String requestLine = br.readLine();
        StringTokenizer st = new StringTokenizer(requestLine, " ");

        String method = st.nextToken();
        String uri = st.nextToken();
        String httpVersion = st.nextToken();

        logger.debug("----------------------------------------------------------------------------------------------");
        logger.debug(requestLine);

        while(!(requestLine = br.readLine()).isEmpty()){
            logger.debug(requestLine);
        }
        logger.debug("----------------------------------------------------------------------------------------------");

        return uri;
    }
}
