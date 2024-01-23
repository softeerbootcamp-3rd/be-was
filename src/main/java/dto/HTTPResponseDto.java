package dto;

import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
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

    public HTTPResponseDto(int statusCode, String content, byte[] body) {
        this.statusCode = statusCode;
        this.header = new HashMap<>();
        // 상태 코드에 맞는 enum 상수 반환
        ResponseEnum responseEnum = ResponseEnum.getResponse(statusCode);
        if(body == null)
            // 상태 코드에 맞는 헤더 내용 추가
            setHeader(responseEnum.addHeader(this.header, content, null));
        else {
            this.body = body;
            // 상태 코드에 맞는 헤더 내용 추가
            setHeader(responseEnum.addHeader(this.header, content, body.length));
        }
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
        // TODO 2. header 작성
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'");
        String formattedDateTime = currentDateTime.format(formatter);
        addHeader("Date", formattedDateTime);
    }
}
