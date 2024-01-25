package webserver.io;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.ExceptionResolver;
import webserver.MyHttpServletRequest;
import webserver.MyHttpServletResponse;
import webserver.handler.ControllerHandler;
import webserver.handler.Handler;
import webserver.handler.StaticResourceHandler;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final ExceptionResolver exceptionResolver = new ExceptionResolver();
    private final List<Handler> handlers = new ArrayList<>();
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        handlers.add(new StaticResourceHandler());
        handlers.add(new ControllerHandler());
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            MyHttpServletRequest httpServletRequest = printReceivedRequest(in);
            logger.debug("http request : {}",httpServletRequest.toString());

            MyHttpServletResponse httpResponse = null;
            try {
                Handler handler = null;
                for(Handler h : handlers){
                    if(h.canHandle(httpServletRequest))
                        handler=h;
                }
                if(handler==null)
                    throw new RuntimeException();
                httpResponse = handler.handle(httpServletRequest);
            }catch (Exception e){
                httpResponse = exceptionResolver.resolve(e);
            }
            HttpResponseBuilder responseBuilder = new HttpResponseBuilder(new DataOutputStream(out));
            //responseBuilder에게 socket에 http응답정보를 작성하도록 요청한다.
            responseBuilder.flushHttpResponse(httpResponse);

        } catch (IOException e) {
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
