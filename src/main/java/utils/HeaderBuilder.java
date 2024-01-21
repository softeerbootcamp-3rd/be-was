package utils;

import dto.ResponseDto;

public class HeaderBuilder {

    public static String build(ResponseDto responseDto) {
        if (responseDto.getCode() == 400) {
            return header400();
        }
        if (responseDto.getCode() == 404) {
            return header404();
        }
        if (responseDto.getCode() == 302) {
            return header302(responseDto);
        }
        return header200(responseDto);
    }

    private static String header400() {
        return "HTTP/1.1 404 Not Found \r\n"
                + "\r\n";
    }

    private static String header404() {
        return "HTTP/1.1 404 Not Found \r\n"
                + "\r\n";
    }

    private static String header302(ResponseDto responseDto) {
        return "HTTP/1.1 302 Found \n"
                + "Location: " + responseDto.getUrl() + "\r\n"
                + "\r\n";
    }

    private static String header200(ResponseDto responseDto) {
        return "HTTP/1.1 200 OK \n"
                + "HTTP/1.1 200 OK \r\n"
                + "Content-Type: " + contentType(responseDto.getUrl())
                + ";charset=utf-8\r\n"
                + "Content-Length: " + responseDto.getBody().length + "\r\n"
                + "\r\n";
    }

    private static String contentType(String url) {
        if (url.endsWith(".css")) {
            return "text/css";
        }
        if (url.endsWith(".js")) {
            return "text/javascript";
        }
        if (url.endsWith(".ico")) {
            return "image/x-icon";
        }
        if (url.endsWith(".ttf")) {
            return "font/ttf";
        }
        if (url.endsWith(".woff")) {
            return "font/woff";
        }
        return "text/html";
    }
}
