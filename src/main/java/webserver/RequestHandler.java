package webserver;

import java.io.*;
import java.net.Socket;

import controller.RequestDataController;
import data.RequestData;

import data.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.RequestParserUtil;
import util.ResponseBuilder;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.

            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            // HTTP 요청을 파싱한다.
            RequestData requestData = RequestParserUtil.parseRequest(br);

            // 파싱한 요청의 세부 내용 출력
            logger.debug(requestData.toString());

            Response response = RequestDataController.routeRequest(requestData);

            DataOutputStream dos = ResponseBuilder.buildResponse(out, response, requestData);

            dos.flush();

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
