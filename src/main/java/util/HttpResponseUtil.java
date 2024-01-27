package util;

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
        httpResponseDto.getHeaders().forEach((key, value) -> {
            stringBuilder.append(key).append(": ")
                    .append(value).append("\r\n");
        });
        stringBuilder.append("\r\n");

        return stringBuilder.toString();
    }

    // 리소스 요청 처리
    public static HttpResponseDto loadResource(HttpRequestDto request, Logger logger) {
        HttpResponseDtoBuilder responseDtoBuilder = new HttpResponseDtoBuilder();
        byte[] body = null;

        File file = new File(WebUtil.getPath(request.getUri()));

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[(int) file.length()];
            fileInputStream.read(buffer);
            if (WebUtil.getFileExtension(request.getUri()).equals("html")) {
                // html 요청인 경우 navBar 변경
                body = HtmlBuilder.buildPage(request, new String(buffer));
            } else {
                body = buffer;
            }
        } catch (IOException e) {
            logger.error(e.getMessage());

            return responseDtoBuilder.response404Header().build();
        }

        return responseDtoBuilder.response200Header()
                .setHeaders("Content-Type", WebUtil.getContentType(request.getUri()) + ";charset=utf-8")
                .setHeaders("Content-Length", Integer.toString(body.length))
                .setBody(body)
                .build();
    }
}
