package webserver;

import java.io.*;
import java.net.Socket;
import java.util.function.Function;

import dto.RequestDto;
import dto.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.ControllerMapper;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.

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
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
