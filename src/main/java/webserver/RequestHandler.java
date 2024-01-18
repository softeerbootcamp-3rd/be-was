package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;

import controller.Controller;
import dto.HTTPRequestDto;
import dto.HTTPResponseDto;
import dto.ResponseEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    private HTTPRequestDto httpRequestDto = new HTTPRequestDto();

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

            if(httpRequestDto.getRequestTarget().contains("?")) {
                // request param 맵에 저장
                getRequestParams(httpRequestDto.getRequestTarget());
                // 요청에서 쿼리 스트링 떼어내기
                httpRequestDto.setRequestTarget(
                        httpRequestDto.getRequestTarget().substring(0, httpRequestDto.getRequestTarget().indexOf("?"))
                );
            }

            // 요청 처리
            HTTPResponseDto httpResponseDto = Controller.doRequest(httpRequestDto);

            // status code에 따른 분기 처리 - response DataOutputStream에 작성
            ResponseEnum responseEnum = ResponseEnum.getResponse(httpResponseDto.getStatusCode());
            responseEnum.writeResponse(httpResponseDto, httpRequestDto, dos);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    // DataOutputStream에 response 내용 write
    private void response(byte[] responseHeader, byte[] responseBody, DataOutputStream dos) throws IOException {
        dos.write(responseHeader, 0, responseHeader.length);
        dos.writeBytes("\r\n");
        if(responseBody != null)
            dos.write(responseBody, 0, responseBody.length);
        dos.flush();
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

        httpRequestDto.setHTTPMethod(tokens[0]);
        httpRequestDto.setRequestTarget(tokens[1]);
        httpRequestDto.setHTTPVersion(tokens[2]);

        logger.debug("HTTP Method: {}, Request Target: {}, Version: {}",
                httpRequestDto.getHTTPMethod(),
                httpRequestDto.getRequestTarget(),
                httpRequestDto.getHTTPVersion());

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

    // 쿼리 스트링 파싱
    private void getRequestParams(String url) {
        if (url == null)
            return;
        if (!url.contains("?"))
            return;

        String[] tokens = url.split("\\?");
        tokens = tokens[1].split("&");
        for (int i = 0; i < tokens.length; i++) {
            String key = tokens[i].substring(0, tokens[i].indexOf("="));
            String value = tokens[i].substring(tokens[i].indexOf("=") + 1);
            httpRequestDto.addRequestParam(key, value);
        }
    }

}
