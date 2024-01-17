package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;

import controller.Controller;
import dto.HTTPRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    private HTTPRequestDto httpRequestDto = new HTTPRequestDto();
    private HashMap<String, String> requestParams = new HashMap<>();
    private byte[] body;

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

            // HTTP Request 파싱
            httpRequestParsing(br);

            // 요청에서 Request param 떼어내기
            if(httpRequestDto.getRequest_target().contains("?")) {
                getRequestParams(httpRequestDto.getRequest_target());
                httpRequestDto.setRequest_target(
                        httpRequestDto.getRequest_target().substring(0, httpRequestDto.getRequest_target().indexOf("?"))
                );
            }

            // 요청 처리
            body = Controller.doRequest(httpRequestDto, requestParams);

            response200Header(dos, body.length, httpRequestDto.getAccept());
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    // HTTP Request 파싱
    private void httpRequestParsing(BufferedReader br) throws IOException {
        // 요청 라인 읽어오기
        String line = br.readLine();
        if(line == null)
            return;
        // start line 파싱
        // 한글 파라미터 decoding
        line = URLDecoder.decode(line, "UTF-8");
        String[] tokens = line.split(" ");

        httpRequestDto.setHTTP_Method(tokens[0]);
        httpRequestDto.setRequest_target(tokens[1]);
        httpRequestDto.setHTTP_version(tokens[2]);

        logger.debug("HTTP Method: {}, Request Target: {}, Version: {}",
                httpRequestDto.getHTTP_Method(),
                httpRequestDto.getRequest_target(),
                httpRequestDto.getHTTP_version());

        // host, accept 출력
        while(!line.equals("")) {
            line = br.readLine();
            if(line.contains("Host:")) {
                httpRequestDto.setHost(line.substring("Host: ".length()));
                logger.debug("Host: {}", httpRequestDto.getHost());
            }
            else if(line.contains("Accept:")) {
                // Accept 추출
                String accept = line.substring("Accept: ".length());
                if(line.contains(","))
                    accept = accept.substring(0, accept.indexOf(","));
                httpRequestDto.setAccept(accept);
                logger.debug("Accept: {}", httpRequestDto.getAccept());
            }
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
    private void getRequestParams(String url) {
        if(url == null)
            return;
        if(!url.contains("?"))
            return;

        String[] tokens = url.split("\\?");
        tokens = tokens[1].split("&");
        for(int i = 0; i < tokens.length; i++) {
            String key = tokens[i].substring(0, tokens[i].indexOf("="));
            String value = tokens[i].substring(tokens[i].indexOf("=")+1);
            requestParams.put(key, value);
        }
    }

}
