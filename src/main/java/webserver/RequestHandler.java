package webserver;

import java.io.*;
import java.net.Socket;
import java.util.function.BiConsumer;

import constant.ErrorCode;
import constant.HttpStatus;
import dto.RequestDto;
import exception.WebServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FileManager;
import util.MethodMapper;
import util.RequestParser;


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
            RequestDto requestDto = RequestParser.getRequestDto(in);
            DataOutputStream dos = new DataOutputStream(out);

            BiConsumer<DataOutputStream, RequestDto> method;
            byte[] body;

            if ((method = MethodMapper.getMethod(requestDto.getMethodAndPath())) != null) {
                try {
                    method.accept(dos, requestDto);
                } catch (WebServerException e) {
                    ResponseHandler.sendError(dos, e.getErrorCode());
                }
            } else if (requestDto.getMethod().equals("GET") && (body = FileManager.getFileByPath(requestDto.getPath())) != null) {
                ResponseHandler.sendBody(dos, HttpStatus.OK, body);
            } else {
                ResponseHandler.sendError(dos, ErrorCode.PAGE_NOT_FOUND);
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
