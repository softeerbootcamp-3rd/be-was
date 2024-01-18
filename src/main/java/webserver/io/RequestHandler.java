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

            //ControllerMapper로부터 요청을 수행할 수 있는 Method를 가져온다.
            controllerMapper.findAppropriateControllerMethod(controllerHandler,httpServletRequest.getUri());
            //ParameterMapper로부터 Method의 파라미터들을 MyHttpServletRequest가 가지고 있는 쿼리 파라미터에서 추출해 주입한다.
            parameterMapper.findAppropriateParameter(controllerHandler,httpServletRequest);
            //Method를 실행할 수 있는 조건(실행할 Method, Method에 필요한 파라미터값, 실행할 Method를 가지고 있는 객체)이
            //다 갖춰졌기 때문에 해당 Method를 ControllerHandler가 실행한다.
            MyHttpServletResponse httpResponse = controllerHandler.handleController();

            this.responseBuilder= new HttpResponseBuilder(new DataOutputStream(out));
            //responseBuilder에게 socket에 http응답정보를 작성하도록 요청한다.
            responseBuilder.flushHttpResponse(httpResponse);

        } catch (IOException | InvocationTargetException | IllegalAccessException e) {
            logger.error(e.getMessage());
        }
    }

    private MyHttpServletRequest printReceivedRequest(InputStream in) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
        String line = URLDecoder.decode(br.readLine(), StandardCharsets.UTF_8);
        logger.debug("request line : {}",line);
        //Http Method & Version & URI 파싱
        MyHttpServletRequest servletRequest = MyHttpServletRequest.init(line);
        if(servletRequest==null)
            return null;
        //Http Header 파싱
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
