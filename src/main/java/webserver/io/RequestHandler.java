package webserver.io;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.MyHttpServletRequest;
import webserver.mapper.ControllerMapper;
import webserver.mapper.ParameterMapper;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final HttpResponseBuilder responseBuilder;
    private final ControllerHandler controllerHandler = new ControllerHandler();
    private final ControllerMapper controllerMapper = new ControllerMapper();
    private final ParameterMapper parameterMapper = new ParameterMapper();
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.responseBuilder=new HttpResponseBuilder();
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            MyHttpServletRequest httpServletRequest = printReceivedRequest(in);
            logger.debug("http request : {}",httpServletRequest.toString());

            controllerMapper.findAppropriateControllerMethod(controllerHandler,httpServletRequest.getUri());
            parameterMapper.findAppropriateParameter(controllerHandler,httpServletRequest);
            String resourcePath = controllerHandler.handleController();

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = responseBuilder.getFileBytes(resourcePath);
            response200Header(dos, body.length);
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

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private MyHttpServletRequest printReceivedRequest(InputStream in) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
        String line = br.readLine();
        logger.debug("request line : {}",line);
        MyHttpServletRequest servletRequest = MyHttpServletRequest.init(line);
        if(servletRequest==null)
            return null;
        while(true){
            line=br.readLine();
            if (line.equals(""))
                break;
            logger.debug("header : {}",line);
            servletRequest.setFieldByName(line);
        }
        return servletRequest;
    }


}
