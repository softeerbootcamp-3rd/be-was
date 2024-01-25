package util;

import dto.HttpResponseDto;

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
}
