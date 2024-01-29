package webserver;

import java.io.*;
import java.net.Socket;
import httpmessage.request.HttpMesssageReader;
import httpmessage.request.HttpRequest;
import httpmessage.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import controller.*;

public class RequestHandler implements Runnable {
    private Socket connection;
    private final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpResponse httpResponse = new HttpResponse();
            HttpRequest httpRequest = new HttpRequest(new HttpMesssageReader(br));

            logger.debug(">> {}:{}",httpRequest.getHttpMethod(),httpRequest.getPath());

            FirstController firstController = new FirstController();
            firstController.service(httpRequest,httpResponse);

            DataOutputStream dos = new DataOutputStream(out);

            if(httpResponse.getStatusCode()==302){
                response302Header(dos,httpResponse);
            }

            else {
                responseHeader(dos, httpResponse);
                responseBody(dos, httpResponse);
            }
        }
        catch (IOException e) {
            logger.error(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void responseHeader(DataOutputStream dos, HttpResponse httpResponse) throws IOException {

        dos.writeBytes("HTTP/1.1 " + httpResponse.getStatusCode() + " " + httpResponse.getStatusLine() + "\r\n");
        dos.writeBytes("Content-Type: " + httpResponse.getContentType() + "\r\n");
        dos.writeBytes("Content-Length: " + httpResponse.getBody().length + "\r\n");
        dos.writeBytes("\r\n");
    }

    void response302Header(DataOutputStream dos, HttpResponse httpResponse) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found\r\n");
        dos.writeBytes("Location: "+ httpResponse.getPath() + "\r\n");

        if(!httpResponse.getSid().isEmpty()){
            dos.writeBytes("Set-Cookie: sid=" + httpResponse.getSid()+ "; Expires=" + httpResponse.getExpireDate() + "; Path=/");
        }

        dos.writeBytes("\r\n");
        dos.flush();
    }
    private void responseBody(DataOutputStream dos, HttpResponse httpResponse) {
        try {
            dos.write(httpResponse.getBody(), 0, httpResponse.getBody().length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
