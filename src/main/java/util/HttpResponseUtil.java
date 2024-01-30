package util;

import constant.HttpHeader;
import dto.HttpRequestDto;
import dto.HttpResponseDto;
import dto.HttpResponseDtoBuilder;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class HttpResponseUtil {

    // Build HTTP Response Message (String) from HttpResponseDto
    public static String responseHeaderBuilder(HttpResponseDto httpResponseDto) {
        StringBuilder stringBuilder = new StringBuilder();
        // Status line
        stringBuilder.append(httpResponseDto.getHttpVersion()).append(" ")
                .append(httpResponseDto.getStatus()).append(" ")
                .append(httpResponseDto.getMessage()).append(" \r\n");
        // Response Headers
        stringBuilder.append(httpResponseDto.getHeaders().buildResponseHeader());
        stringBuilder.append("\r\n");

        return stringBuilder.toString();
    }

    // 리소스 요청 처리
    public static HttpResponseDto loadResource(HttpRequestDto request, Logger logger) {
        HttpResponseDtoBuilder responseDtoBuilder = new HttpResponseDtoBuilder();
        byte[] body = null;

        File file = new File(HttpRequestParser.getPath(request.getUri()));

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[(int) file.length()];
            fileInputStream.read(buffer);

            if (HttpRequestParser.getFileExtension(request.getUri()).equals("html")) {
                // html 요청인 경우 navBar 변경
                body = HtmlBuilder.buildPage(request, new String(buffer));
            } else {
                body = buffer;
            }
        } catch (IOException e) {
            logger.error(e.getMessage());

            return buildErrorResponse("404", "Not Found", e.getMessage());
        }

        return responseDtoBuilder.response200Header()
                .setHeaders(HttpHeader.CONTENT_TYPE, HttpRequestParser.getContentType(request.getUri()) + ";charset=utf-8")
                .setHeaders(HttpHeader.CONTENT_LENGTH, Integer.toString(body.length))
                .setBody(body)
                .build();
    }

    public static HttpResponseDto buildErrorResponse(String statusCode, String statusMsg, String errorMsg) {
        HttpResponseDtoBuilder responseDtoBuilder = new HttpResponseDtoBuilder();
        byte[] body = HtmlBuilder.buildErrorPage(statusCode, statusMsg, errorMsg);

        return responseDtoBuilder
                .setStatus(statusCode).setMessage(statusMsg)
                .setHeaders(HttpHeader.CONTENT_TYPE, "text/html;charset=utf-8")
                .setHeaders(HttpHeader.CONTENT_LENGTH, Integer.toString(body.length))
                .setBody(body)
                .build();
    }
}
