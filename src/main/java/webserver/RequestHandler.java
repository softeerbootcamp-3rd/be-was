package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.file.Files;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            DataOutputStream dos = new DataOutputStream(out);
            // 헤더 값 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            // 요청 라인 읽어오기
            String line = br.readLine();
            if(line == null)
                return;
            // 요청 URL 추출
            String[] tokens = line.split(" ");
            // 한글 파라미터 decoding
            String url = URLDecoder.decode(tokens[1], "UTF-8");

            String accept = "";
            logger.debug("HTTP Method: {}, Request Target: {}", tokens[0], url);
            // host, accept 출력
            while(!line.equals("")) {
                line = br.readLine();
                if(line.contains("Host"))
                    logger.debug(line);
                else if(line.contains("Accept:")) {
                    // Accept 추출
                    accept = line.substring("Accept: ".length());
                    if(line.contains(","))
                        accept = accept.substring(0, accept.indexOf(","));
                    logger.debug("Accept: {}", accept);
                }
            }

            byte[] body = "".getBytes();     // 초기화

            // 요청에서 Request param 떼어내기
            String[] requestParams = null;
            if(url.contains("?")) {
                requestParams = getRequestParams(url);
                url = url.substring(0, url.indexOf("?"));
            }

            // 파일 불러오기 외의 요청
            // 1. 회원가입 처리
            if(url.equals("/user/create")) {
                User user = signup(requestParams);
                if(user == null)
                    body = "다시 시도해주세요.".getBytes();
                else {
                    logger.debug(user.toString());
                    body = ("Hello, " + user.getName() + "!").getBytes();
                }
            }

            // 파일 불러오기 요청
            else {
                // 해당 파일을 읽고 응답
                String path = "./src/main/resources";
                // 1. html일 경우
                if(url.contains(".html")) {
                    path += "/templates" + url;
                }
                // 2. css, fonts, images, js, ico일 경우
                else {
                    path += "/static" + url;
                }

                logger.debug("path: {}", path);
                body = Files.readAllBytes(new File(path).toPath());
            }

            response200Header(dos, body.length, accept);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String accept) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + accept + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    // 요청 url에서 Request Param 리스트 가져오기
    private String[] getRequestParams(String url) {
        if(url == null)
            return null;
        if(!url.contains("?"))
            return null;

        String[] tokens = url.split("\\?");
        tokens = tokens[1].split("&");
        for(int i = 0; i < tokens.length; i++)
            tokens[i] = tokens[i].substring(tokens[i].indexOf("=")+1);

        return tokens;
    }

    // 회원가입 처리
    private User signup(String[] requestParams) {
        if(requestParams == null || requestParams.length != 4)
            return null;
        // User 객체 생성
        User user = new User(requestParams[0], requestParams[1], requestParams[2], requestParams[3]);
        return user;
    }
}
