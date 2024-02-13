package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import controller.Controller;
import dto.request.HTTPRequestDto;
import dto.request.HeaderEnum;
import dto.response.HTTPResponseDto;
import dto.response.ResponseEnum;
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

            // query string 파싱
            queryStringParsing(httpRequestDto.getRequestTarget());

            // 요청 처리
            HTTPResponseDto httpResponseDto = Controller.doRequest(httpRequestDto);

            // status code에 맞는 응답 반환
            writeResponse(dos, httpResponseDto);

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
        Map<String, String> header = new HashMap<String, String>();

        while(true) {
            String line = br.readLine();
            if(line == null)
                break;
            // 개행문자만을 읽었다면 -> 헤더 끝
            if(line != null && line.isEmpty())
                break;
            if(!line.contains(":"))
                continue;
            String key = line.substring(0, line.indexOf(":")).strip();

            // header enum을 순회하며 파싱의 대상일 경우 파싱 -> header 맵에 저장
            HeaderEnum headerEnum = HeaderEnum.getHeaderKey(key);
            header = headerEnum.doParsing(header, line);
        }
        httpRequestDto.setHeader(header);
    }

    // http request 파싱 3: body
    private void bodyParsing(BufferedReader br) throws IOException {
        Integer length;
        if( (length = httpRequestDto.getContentLength()) != null) {
            char[] body = new char[length];
            br.read(body);

            // 한글 파라미터 decoding 후 body 저장
            bodyMapParsing(URLDecoder.decode(new String(body), StandardCharsets.UTF_8));
            logger.debug("Request Body: {}", httpRequestDto.getBody().getMap().toString());
        }
    }

    // 쿼리 스트링 파싱
    private void queryStringParsing(String url) {
        if (url == null)
            return;
        if (!url.contains("?"))
            return;

        Map<String, String> requestParams = new HashMap<>();
        String params = url.substring(url.indexOf("?")+1);
        String[] param = params.split("&");
        for (int i = 0; i < param.length; i++) {
            String[] elements = param[i].split("=");
            requestParams.put(elements[0], elements[1]);
        }
        httpRequestDto.setRequestParam(requestParams);
        // 요청에서 쿼리 스트링 떼어내기
        httpRequestDto.setRequestTarget(
                httpRequestDto.getRequestTarget().substring(0, httpRequestDto.getRequestTarget().indexOf("?"))
        );
    }

    // body 파싱
    private void bodyMapParsing(String body) {
        HashMap<String, String> bodyMap = new HashMap<>();
        String[] tokens = body.split("&");
        for(int i = 0; i < tokens.length; i++) {
            String key = tokens[i].substring(0, tokens[i].indexOf("="));
            String value = tokens[i].substring(tokens[i].indexOf("=") + 1);
            bodyMap.put(key, value);
        }
        httpRequestDto.setBody(bodyMap);
    }

    // response 응답
    private void writeResponse(DataOutputStream dos, HTTPResponseDto httpResponseDto) throws IOException {
        // status code에 맞는 enum 상수 가져오기
        ResponseEnum responseEnum = ResponseEnum.getResponse(httpResponseDto.getStatusCode());

        // 1. status line 작성
        dos.writeBytes(responseEnum.getStatusLine());
        // header에 Date 추가
        httpResponseDto.setDate();
        // header에 Connection 추가
        httpResponseDto.setConnection(responseEnum);
        // 2. header 작성
        for(Map.Entry<String, String> entry: httpResponseDto.getHeader().entrySet()) {
            if(entry.getKey().equals("Set-Cookie")) {               // 쿠키는 여러 개일 수 있으므로 value에 전체 헤더를 저장
                dos.writeBytes(entry.getValue());
                continue;
            }
            dos.writeBytes(entry.getKey() + ": " + entry.getValue() + "\r\n");
        }
        // 3. body 작성
        if(httpResponseDto.getBody() != null) {
            dos.writeBytes("\r\n");
            dos.write(httpResponseDto.getBody());
        }
        dos.flush();
    }

}
