package dto.response;

import dto.request.FirstClassCollection;
import dto.request.HTTPRequestDto;

import java.awt.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HTTPResponseDto {

    private int statusCode;
    private Map<String, String> header;
    private byte[] body;

    public HTTPResponseDto(int statusCode, String contentType, byte[] body) {
        this.statusCode = statusCode;
        this.header = new HashMap<>();
        this.body = body;
        setContentTypeAndContentLength(contentType, body.length);
    }
    public HTTPResponseDto(String location) {
        this.statusCode = 302;
        this.header = new HashMap<>();
        setHeader("Location", location);
    }

    public int getStatusCode() {
        return this.statusCode;
    }
    public Map<String, String> getHeader() {
        return this.header;
    }
    public byte[] getBody() {
        return this.body;
    }

    public void setHeader(String key, String value) {
        header.put(key, value);
    }
    // header에 Date 추가
    public void setDate() {
        // 현재 날짜 및 시간 가져오기
        LocalDateTime currentDateTime = LocalDateTime.now();
        // 날짜 및 시간 포맷 지정 (예: "Sat, 01 Jan 2022 12:00:00 GMT")
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'").withLocale(Locale.ENGLISH);
        String formattedDateTime = currentDateTime.format(formatter);
        setHeader("Date", formattedDateTime);
    }
    // header에 connection 추가
    public void setConnection(ResponseEnum responseEnum) {
        setHeader("Connection", responseEnum.getConnection());
    }
    // Header에 Content-Type, Content-Length 추가
    public void setContentTypeAndContentLength(String contentType, Integer contentLength) {
        if(contentType != null)
            setHeader("Content-Type", contentType + "; charset=utf-8");
        if(contentLength != null)
            setHeader("Content-Length", "" + contentLength);
    }

}
