package webserver;

import java.io.*;
import java.net.Socket;

import constant.ErrorCode;
import constant.FilePath;
import dto.RequestDto;
import dto.ResponseDto;
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

            // TODO 분석한 요청의 경로를 이용해, 로그인한 사용자만 사용 가능한 건지 확인 후 처리

            FilePath basePath = FilePath.HTML_BASE;
            byte[] body;

            if (requestDto.getPath().equals("/")) { requestDto.setPath(MAIN_PAGE.path); }
            if (!requestDto.getPath().endsWith(".html")) { basePath = FilePath.SUPPORT_FILE_BASE; }

            ResponseDto response = new ResponseDto();

            if (MethodMapper.hasMethod(requestDto.getMethodAndPath())) {
                response = MethodMapper.execute(requestDto);
            } else if (requestDto.getMethod().equals("GET") &&
                    (body = FileManager.getFileByPath(basePath, requestDto.getPath())) != null) {
                String contentType = FileManager.getContentType(requestDto.getPath());
                response.makeBody(body, contentType);
            } else {
                response.makeError(ErrorCode.PAGE_NOT_FOUND);
            }

            ResponseHandler.send(dos, response);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
