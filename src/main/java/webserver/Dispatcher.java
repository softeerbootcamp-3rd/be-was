package webserver;

import java.io.*;
import java.net.Socket;

import constant.ErrorCode;
import constant.StaticFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FileManager;
import util.MethodMapper;
import util.RequestParser;


public class RequestDispatcher implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestDispatcher.class);

    private Socket connection;

    public RequestDispatcher(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            // 유효한 연결인지 확인
            if (isValidConnection(in, out)) {

                // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
                HttpRequest httpRequest = RequestParser.getHttpRequest(in);
                DataOutputStream dos = new DataOutputStream(out);

                // TODO 분석한 요청의 경로를 이용해, 로그인한 사용자만 사용 가능한 건지 확인 후 처리

                String basePath = StaticFile.HTML_BASE_PATH;
                byte[] body;

                // 루트 페이지로 요청이 들어온 경우에도 메인 페이지로 이동할 수 있도록
                if (httpRequest.getPath().equals("/")) { httpRequest.setPath(StaticFile.MAIN_PAGE_PATH); }
                // html 파일이 아닌 경우, 파일의 기본 경로를 resources/static 으로 변경
                if (!httpRequest.getPath().endsWith(".html")) { basePath = StaticFile.SUPPORT_FILE_BASE_PATH; }

                HttpResponse httpResponse = new HttpResponse();

                if (MethodMapper.hasMethod(httpRequest.getMethod() + httpRequest.getPath())) {
                    httpResponse = MethodMapper.execute(httpRequest);
                } else if (httpRequest.getMethod().equals("GET") &&
                        (body = FileManager.getFileByPath(basePath, httpRequest.getPath())) != null) {
                    String contentType = FileManager.getContentType(httpRequest.getPath());
                    httpResponse.makeBody(body, contentType);
                } else {
                    httpResponse.makeError(ErrorCode.PAGE_NOT_FOUND);
                }

                ResponseHandler.send(dos, httpResponse);
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private boolean isValidConnection(InputStream in, OutputStream out) throws IOException {
        if (in == null || out == null) {
            if (in != null) in.close();
            if (out != null) out.close();
            return false;
        }
        return true;
    }

}
