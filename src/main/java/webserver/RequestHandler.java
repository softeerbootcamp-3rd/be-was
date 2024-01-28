package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;

import controller.Controller;
import dto.request.HTTPRequestDto;
import dto.response.HTTPResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {

    private Socket connection;
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

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

            // status code에 맞는 응답 반환
            httpResponseDto.writeResponse(dos);

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
        startLineParsing(line);

        // header 파싱
        headerParsing(br);

        // body 읽기
        bodyParsing(br);
    }

    // http request 파싱 1: start line
    private void startLineParsing(String startLine) throws IOException {
        // start line 파싱
        // 한글 파라미터 decoding
        startLine = URLDecoder.decode(startLine, "UTF-8");
        String[] tokens = startLine.split(" ");

        httpRequestDto.setHTTPMethod(tokens[0]);
        httpRequestDto.setRequestTarget(tokens[1]);
        httpRequestDto.setHTTPVersion(tokens[2]);

        logger.debug("HTTP Method: {}, Request Target: {}, Version: {}",
                httpRequestDto.getHTTPMethod(),
                httpRequestDto.getRequestTarget(),
                httpRequestDto.getHTTPVersion());
    }

    // http request 파싱 2: header
    private void headerParsing(BufferedReader br) throws IOException {
        String key, value;

        while(true) {
            String line = br.readLine();
            if(line == null)
                break;
            // 개행문자만을 읽었다면 -> 헤더 끝
            if(line != null && line.isEmpty())
                break;
            if(line.contains("Host:")) {
                // Host 추출
                oneHeaderParsing("Host", line);
                continue;
            }
            if(line.contains("Accept:")) {
                // Accept 추출
                oneHeaderParsing("Accept", line);
                continue;
            }
            if(line.contains("Connection:")) {
                // Connection 추출
                oneHeaderParsing("Connection", line);
                continue;
            }
            if(line.contains("Content-Length:")) {
                // Content-Length 추출
                oneHeaderParsing("Content-Length", line);
                continue;
            }
            if(line.contains("Cookie:")) {
                // Cookie 추출
                oneHeaderParsing("Cookie", line);
                continue;
            }
        }
    }

    // http request 파싱 3: header 하나
    private void oneHeaderParsing(String key, String line) {
        String value = line.substring((key + ": ").length());
        if(value.contains(","))
            value = value.substring(0, value.indexOf(","));
        httpRequestDto.addHeader(key, value);
        logger.debug(key + ": {}",  value);
    }

    // http request 파싱 4: body
    private void bodyParsing(BufferedReader br) throws IOException {
        Integer length;
        if( (length = httpRequestDto.getContentLength()) != null) {
            char[] body = new char[length];
            br.read(body);

            // 한글 파라미터 decoding 후 body 저장
            httpRequestDto.setBody(URLDecoder.decode(new String(body), "UTF-8"));
            logger.debug("Request Body: {}", httpRequestDto.getBody());
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
