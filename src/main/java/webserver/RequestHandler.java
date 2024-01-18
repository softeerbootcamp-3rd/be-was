package webserver;

import java.io.*;
import java.net.Socket;

import config.HTTPRequest;
import config.HTTPResponse;
import config.ResponseCode;
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


            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            logger.debug("Ending Connected IP : {}, Port : {}", connection.getInetAddress(),
                    connection.getPort());

            HTTPRequest request = new HTTPRequest(br, logger);
            DataOutputStream dos = new DataOutputStream(out);
            String url = request.getUrl();


            ControllerHandler controllerHandler = null;
            String urlFrontPart = url.split("\\?")[0];
            for (ControllerHandler handler : ControllerHandler.values()) {
                if (handler.url.equals(urlFrontPart)) {
                    controllerHandler = handler;
                    break;
                }
            }

            // 적절한 컨트롤러 핸들러를 찾았을 경우, 해당 핸들러의 메소드 호출
            HTTPResponse response;
            if (controllerHandler != null)
                response = controllerHandler.toController(request);
            else
                response = new HTTPResponse("HTTP/1.1", ResponseCode.NOT_FOUND.code, ResponseCode.NOT_FOUND.toString());

            byte[] head = response.getHead();
            byte[] body = response.getBody();

            dos.write(head, 0, head.length);
            dos.writeBytes("\r\n");
            dos.write(body, 0, body.length);
            dos.flush();



        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
//아래는 기존의 코드
//    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
//        try {
//            dos.writeBytes("HTTP/1.1 200 OK \r\n");
//            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
//            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
//            dos.writeBytes("\r\n");
//            //\r\n 두개는 헤더와 바디를 구분하게 해준다
//            logger.debug(dos.toString());
//        } catch (IOException e) {
//            logger.error(e.getMessage());
//        }
//    }
//
//    private void responseBody(DataOutputStream dos, byte[] body) {
//        try {
//            //dos
//            dos.write(body, 0, body.length);
//            //flush를 사용하면 dos에 있는 내용을 전부 보낸다
//            dos.flush();
//        } catch (IOException e) {
//            logger.error(e.getMessage());
//        }
//    }


}
