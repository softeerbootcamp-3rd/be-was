package dto.response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HTTPResponseDto {
    private int statusCode;
    private HashMap<String, String> header;
    private byte[] body;

    public HTTPResponseDto() {
        this.statusCode = 500;
        this.header = new HashMap<>();
        this.body = null;
    }
    public HTTPResponseDto(int statusCode) {
        this.statusCode = statusCode;
        this.header = new HashMap<>();
        this.body = null;
    }
    public HTTPResponseDto(int statusCode, byte[] body) {
        this.statusCode = statusCode;
        this.header = new HashMap<>();
        this.body = body;
    }

    public int getStatusCode() {
        return this.statusCode;
    }
    public HashMap<String, String> getHeader() {
        return this.header;
    }
    public byte[] getBody() {
        return this.body;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    public void setHeader(HashMap<String, String> header) {
        this.header = header;
    }
    public void setBody(byte[] body) {
        this.body = body;
    }
    public void addHeader(String key, String value) {
        header.put(key, value);
    }

    // response 반환
    public void writeResponse(DataOutputStream dos) throws IOException {
        // status code에 맞는 enum 상수 가져오기
        ResponseEnum responseEnum = ResponseEnum.getResponse(this.statusCode);

        // 1. status line 작성
        dos.writeBytes(responseEnum.getStatusline());
        // header에 Date 추가
        addDate2Header();
        // header에 Connection 추가
        setHeader(responseEnum.addConnection2Header(this.header));
        // 2. header 작성
        for(Map.Entry<String, String> entry: header.entrySet())
            dos.writeBytes(entry.getKey() + ": " + entry.getValue() + "\r\n");
        // 3. body 작성
        if(body != null) {
            dos.writeBytes("\r\n");
            dos.write(body);
        }
        dos.flush();
    }

    // header에 Date 추가
    public void addDate2Header() {
        // 현재 날짜 및 시간 가져오기
        LocalDateTime currentDateTime = LocalDateTime.now();
        // 날짜 및 시간 포맷 지정 (예: "Sat, 01 Jan 2022 12:00:00 GMT")
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'").withLocale(Locale.ENGLISH);
        String formattedDateTime = currentDateTime.format(formatter);
        addHeader("Date", formattedDateTime);
    }
    // Header에 Content-Type, Content-Length 추가
    public void addContentTypeAndContentLength2Header(String contentType, Integer contentLength) {
        if(contentType != null)
            addHeader("Content-Type", contentType + "; charset=utf-8");
        if(contentLength != null)
            addHeader("Content-Length", "" + contentLength);
    }

    // 헤더에 content-type, content-length 추가한 응답 반환
    public static HTTPResponseDto createResponseDto(int statusCode, String contentType, byte[] body) {
        HTTPResponseDto httpResponseDto = new HTTPResponseDto(statusCode, body);
        httpResponseDto.addContentTypeAndContentLength2Header(contentType, (body == null) ? null : body.length);
        return httpResponseDto;
    }
}
