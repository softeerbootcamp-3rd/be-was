package webserver.io;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.MyHttpServletRequest;
import webserver.MyHttpServletResponse;
import webserver.mapper.ControllerMapper;
import webserver.mapper.ParameterMapper;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private HttpResponseBuilder responseBuilder;
    private final ControllerHandler controllerHandler = new ControllerHandler();
    private final ControllerMapper controllerMapper = new ControllerMapper();
    private final ParameterMapper parameterMapper = new ParameterMapper();
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
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
            MyHttpServletResponse httpResponse = controllerHandler.handleController();

            this.responseBuilder= new HttpResponseBuilder(new DataOutputStream(out));
            responseBuilder.flushHttpResponse(httpResponse);

        } catch (IOException | InvocationTargetException | IllegalAccessException e) {
            logger.error(e.getMessage());
        }
    }

    private MyHttpServletRequest printReceivedRequest(InputStream in) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
        String line = URLDecoder.decode(br.readLine(), StandardCharsets.UTF_8);
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
