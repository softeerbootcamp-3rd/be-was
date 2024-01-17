package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import controller.RequestDataController;
import data.RequestData;

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
            logger.debug(requestData.formatForOutput());
            System.out.println();

            String url = requestData.getRequestContent();

//            if (url.equals("/")) {
//                url = "/index.html";
//            }

            String redirectPath = RequestDataController.routeRequest(url, requestData);

            logger.debug(redirectPath);

            if (redirectPath == null) {
                DataOutputStream dos = new DataOutputStream(out);

                byte[] body = Files.readAllBytes(Paths.get("/Users/admin/Softeer/be-was/src/main/resources/templates" + url));
//                ResponseBuilder.response200Header(dos, body.length);
//                ResponseBuilder.responseBody(dos, body);
                ResponseBuilder.buildResponse(dos, body.length, body, null, "200");
            } else {
                DataOutputStream dos = new DataOutputStream(out);

                byte[] body = Files.readAllBytes(Paths.get("/Users/admin/Softeer/be-was/src/main/resources/templates" + redirectPath));
//                ResponseBuilder.response302Header(dos, "http://localhost:8080/index.html");
//                ResponseBuilder.responseBody(dos, body);
                ResponseBuilder.buildResponse(dos, body.length, body, "http://localhost:8080/index.html", "302");
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1);
        } else {
            return ""; // 확장자가 없는 경우
        }
    }
}
