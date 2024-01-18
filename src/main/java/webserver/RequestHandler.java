package webserver;

import java.io.*;
import java.net.Socket;
import java.util.function.BiConsumer;

import constant.ErrorCode;
import constant.FilePath;
import constant.HttpStatus;
import dto.RequestDto;
import exception.WebServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FileManager;
import util.MethodMapper;
import util.RequestParser;

import static constant.FilePath.MAIN_PAGE;


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
            FilePath basePath = FilePath.HTML_BASE;
            byte[] body;

            if (requestDto.getPath().equals("/")) { requestDto.setPath(MAIN_PAGE.getPath()); }
            if (!requestDto.getPath().endsWith(".html")) { basePath = FilePath.SUPPORT_FILE_BASE; }

            if ((method = MethodMapper.getMethod(requestDto.getMethodAndPath())) != null) {
                try {
                    method.accept(dos, requestDto);
                } catch (WebServerException e) {
                    ResponseHandler.sendError(dos, e.getErrorCode());
                }
            } else if (requestDto.getMethod().equals("GET") &&
                    (body = FileManager.getFileByPath(basePath, requestDto.getPath())) != null) {
                String fileExtension = FileManager.getFileExtension(requestDto.getPath());
                ResponseHandler.sendBody(dos, HttpStatus.OK, body, fileExtension);
            } else {
                ResponseHandler.sendError(dos, ErrorCode.PAGE_NOT_FOUND);
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
