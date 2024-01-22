package util;

import dto.HttpResponseDto;
import dto.HttpResponseDtoBuilder;

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

    public static HttpResponseDto response200(byte[] body, String contentType) {
        return new HttpResponseDtoBuilder().setStatus("200").setMessage("OK")
                .setHeaders("Content-Type", contentType + ";charset=utf-8")
                .setHeaders("Content-Length", Integer.toString(body.length))
                .setBody(body)
                .build();
    }

    public static HttpResponseDto response302(String location) {
        return new HttpResponseDtoBuilder().setStatus("302").setMessage("Found")
                .setHeaders("Location", location)
                .build();
    }

    public static HttpResponseDto response400() {
        return new HttpResponseDtoBuilder().setStatus("400").setMessage("Bad Request")
                .build();
    }

    public static HttpResponseDto response404() {
        return new HttpResponseDtoBuilder().setStatus("404").setMessage("Not Found")
                .build();
    }
}
