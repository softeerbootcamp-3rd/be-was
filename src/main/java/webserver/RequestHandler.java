package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

import dto.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private Request req;
    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.req = new Request();
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            getRequest(in);
            req.requestInfo();
            DataOutputStream dos = new DataOutputStream(out);

            if(!(req.getContentType().isEmpty())) {
                String parentDir = getDir();
                byte[] body = Files.readAllBytes(new File(parentDir + req.getUrl()).toPath());

                response200Header(dos, body.length);
                responseBody(dos, body);
            }
            else{
                byte[] body = new byte[0];
                response301Header(dos, body.length);
                responseBody(dos, body);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response301Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 301 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("Location: " + req.getLocation() + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: "+req.getContentType()+";charset=utf-8\r\n");
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



    private String getDir(){
        if(req.getContentType().equals("text/html")){
            return Paths.get(System.getProperty("user.dir"), "src/main/resources/templates").toString();
        }
        else{
            return Paths.get(System.getProperty("user.dir"), "src/main/resources/static").toString();
        }
    }

    private void getRequest(InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line = bufferedReader.readLine();
        req.setMethod(line.split(" ")[0]);
        req.setUrl(line.split(" ")[1]);

        while (!(line = bufferedReader.readLine()).isEmpty()){
            System.out.println("line = " + line);
        }

    }
}
