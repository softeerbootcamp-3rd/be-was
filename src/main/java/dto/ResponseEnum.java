package dto;

import org.slf4j.Logger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Arrays;

public enum ResponseEnum {
    OK(200) {
        @Override
        public void writeResponse(HTTPResponseDto httpResponseDto, HTTPRequestDto httpRequestDto, DataOutputStream dos) throws IOException {
            response200Header(httpResponseDto, httpRequestDto, dos);
            responseBody(httpResponseDto, dos);
        }
    },
    REDIRECTION(303) {
        @Override
        public void writeResponse(HTTPResponseDto httpResponseDto, HTTPRequestDto httpRequestDto, DataOutputStream dos) throws IOException {
            response303Header(httpResponseDto, dos);
        }
    },
    BAD_REQUEST(404) {
        @Override
        public void writeResponse(HTTPResponseDto httpResponseDto, HTTPRequestDto httpRequestDto, DataOutputStream dos) throws IOException {
            response404Header(httpResponseDto, httpRequestDto, dos);
            responseBody(httpResponseDto, dos);
        }
    },
    SERVER_ERROR(500) {
        @Override
        public void writeResponse(HTTPResponseDto httpResponseDto, HTTPRequestDto httpRequestDto, DataOutputStream dos) throws IOException {
            response500Header(httpResponseDto, httpRequestDto, dos);
        }
    };

    private int statusCode;

    ResponseEnum(int statusCode) {
        this.statusCode = statusCode;
    }

    // status code에 해당하는 상수 반환
    public static ResponseEnum getResponse (int statusCode) {
        return Arrays.stream(ResponseEnum.values())
                .filter(response -> response.statusCode == statusCode)
                .findFirst()
                .orElse(SERVER_ERROR);
    }

    // status code에 맞춰 response 작성
    public abstract void writeResponse(HTTPResponseDto httpResponseDto, HTTPRequestDto httpRequestDto, DataOutputStream dos) throws IOException;

    // 200에 해당하는 response header 작성
    private static void response200Header(HTTPResponseDto httpResponseDto, HTTPRequestDto httpRequestDto, DataOutputStream dos) throws IOException {
        dos.writeBytes("HTTP/1.1 200 OK\r\n");
        dos.writeBytes("Content-Type: " + httpRequestDto.getAccept() + ";charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + httpResponseDto.getContents().length + "\r\n");
        dos.writeBytes("\r\n");
    }

    // response body 작성
    private static void responseBody(HTTPResponseDto httpResponseDto, DataOutputStream dos) throws IOException {
        dos.write(httpResponseDto.getContents(), 0, httpResponseDto.getContents().length);
        dos.flush();
    }

    // 303에 해당하는 response header 작성
    private static void response303Header(HTTPResponseDto httpResponseDto, DataOutputStream dos) throws IOException {
        dos.writeBytes("HTTP/1.1 303 See other\r\n");
        dos.writeBytes("Location: ");
        dos.write(httpResponseDto.getContents(), 0, httpResponseDto.getContents().length);
        dos.writeBytes("\r\n");
        dos.flush();
    }

    // 404에 해당하는 response header 작성
    private static void response404Header(HTTPResponseDto httpResponseDto, HTTPRequestDto httpRequestDto, DataOutputStream dos) throws IOException{
        dos.writeBytes("HTTP/1.1 404 Not Found\r\n");
        dos.writeBytes("Connection: close\r\n");
        dos.writeBytes("Content-Type: " + httpRequestDto.getAccept() + ";charset=utf-8\r\n");
        dos.writeBytes("\r\n");
    }

    // 500에 해당하는 response header 작성
    private static void response500Header(HTTPResponseDto httpResponseDto, HTTPRequestDto httpRequestDto, DataOutputStream dos) throws IOException {
        dos.writeBytes("HTTP/1.1 500 Internal Server Error\r\n");
        dos.writeBytes("Connection: close\r\n");
        dos.writeBytes("Content-Type: " + httpRequestDto.getAccept() + ";charset=utf-8\r\n");
        dos.flush();
    }

    /*
    // 200에 해당하는 response 작성
    private static byte[] response200(HTTPResponseDto httpResponseDto, HTTPRequestDto httpRequestDto) {
        String responseContents = "HTTP/1.1 200 OK \r\n"
                + "Content-Type: " + httpRequestDto.getAccept() + ";charset=utf-8\r\n"
                + "Content-Length: " + httpResponseDto.getContents().length + "\r\n"
                + "\r\n"
                + new String(httpResponseDto.getContents());
        return responseContents.getBytes();
    }

    // 303에 해당하는 response 작성
    private static byte[] response303(HTTPResponseDto httpResponseDto) {
        String responseContents = "HTTP/1.1 303 See other \r\n"
                + "Location: " + new String(httpResponseDto.getContents())
                + "\r\n";
        System.out.print(responseContents);
        return responseContents.getBytes();
    }

    // 404에 해당하는 response 작성
    private static byte[] response404(HTTPResponseDto httpResponseDto, HTTPRequestDto httpRequestDto) {
        String responseContents = "HTTP/1.1 404 Not Found \r\n"
                + "Connection: close"
                + "Content-Type: " + httpRequestDto.getAccept() + ";charset=utf-8\r\n"
                + "Content-Length: " + httpResponseDto.getContents().length + "\r\n"
                + "\r\n"
                + new String(httpResponseDto.getContents());
        return responseContents.getBytes();
    }

    // 500에 해당하는 response 작성
    private static byte[] response500(HTTPResponseDto httpResponseDto, HTTPRequestDto httpRequestDto) {
        String responseContents = "HTTP/1.1 500 Internal Server Error \r\n"
                + "Connection: close"
                + "Content-Type: " + httpRequestDto.getAccept() + ";charset=utf-8\r\n"
                + "Content-Length: " + httpResponseDto.getContents().length + "\r\n"
                + "\r\n";
        return responseContents.getBytes();
    }

     */

}
