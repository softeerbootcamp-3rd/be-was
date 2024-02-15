package util;

import constant.HttpHeader;
import dto.HttpHeaders;
import dto.HttpRequestDtoBuilder;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dto.HttpRequestDto;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestParser.class);

    private static final Map<String, String> MIME_CONTENT_TYPE = new HashMap<>();

    private static final String DEFAULT_CONTENT_LENGTH = "0";

    private static final String DEFAULT_MIME_TYPE = "text/plain";

    static {
        MIME_CONTENT_TYPE.put("html", "text/html");
        MIME_CONTENT_TYPE.put("css", "text/css");
        MIME_CONTENT_TYPE.put("js", "text/javascript");
        MIME_CONTENT_TYPE.put("json", "application/json");
        MIME_CONTENT_TYPE.put("png", "image/png");
        MIME_CONTENT_TYPE.put("jpg", "image/jpeg");
        MIME_CONTENT_TYPE.put("jpeg", "image/jpeg");
        MIME_CONTENT_TYPE.put("gif", "image/gif");
        MIME_CONTENT_TYPE.put("svg", "image/svg+xml");
        MIME_CONTENT_TYPE.put("ico", "image/x-icon");
        MIME_CONTENT_TYPE.put("ttf", "font/ttf");
        MIME_CONTENT_TYPE.put("eot", "application/vnd.ms-fontobject");
        MIME_CONTENT_TYPE.put("woff", "font/woff");
        MIME_CONTENT_TYPE.put("woff2", "font/woff2");
    }

    private static final int BYTE_READ_SIZE = 1024;

    // Parsing HTTP Request message into HttpRequestParams
    public static HttpRequestDto httpRequestParse(InputStream request) {
        HttpRequestDto parsedRequest = null;
        try {
            // InputStream 전체를 byte[] 로 읽어서 targetArray에 저장
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[BYTE_READ_SIZE];
            int bytesRead;

            while ((bytesRead = request.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
                // 무한 루프 막기 위함
                if (bytesRead < BYTE_READ_SIZE) break;
            }

            byte[] targetArray = byteArrayOutputStream.toByteArray();

            // Parsing Request Line: HTTP Request의 첫 줄 파싱
            int index = 0;
            StringBuilder requestLineBuilder = new StringBuilder();
            while (index < targetArray.length && targetArray[index] != '\r' && targetArray[index + 1] != '\n') {
                requestLineBuilder.append((char) targetArray[index]);
                index++;
            }
            index += 2; // Skip '\r\n'
            String[] requestLines = requestLineBuilder.toString().split(" ");

            // Parsing Request Headers: '\r\n\r\n'을 만나기 전까지 파싱
            Map<HttpHeader, String> requestHeaders = new HashMap<>();

            while (index < targetArray.length && targetArray[index] != '\r' && targetArray[index + 1] != '\n') {
                StringBuilder headerLineBuilder = new StringBuilder();
                while (index < targetArray.length && targetArray[index] != '\r' && targetArray[index + 1] != '\n') {
                    headerLineBuilder.append((char) targetArray[index]);
                    index++;
                }
                index += 2; // Skip '\r\n'

                String headerLine = headerLineBuilder.toString();
                String[] requestHeader = headerLine.split(": ");
                try {
                    requestHeaders.put(HttpHeader.fromHeaderName(requestHeader[0]), requestHeader[1]);
                } catch (IllegalArgumentException e) {
                    logger.error(e.getMessage());
                }
            }
            index += 2; // Skip '\r\n'

            // 헤더의 쿠키 값을 이용해 유저 로그인 여부 확인
            User user = SessionUtil.getUserByCookie(requestHeaders);

            // Parsing Request Body: 현재 index부더 끝까지 byte[]로 읽어와서 requestBody에 저장
            ByteArrayOutputStream requestBodyStream = new ByteArrayOutputStream();
            while (index < targetArray.length) {
                requestBodyStream.write(targetArray[index]);
                index++;
            }
            byte[] requestBody = requestBodyStream.toByteArray();

            // Create HttpRequestDto
            parsedRequest = new HttpRequestDtoBuilder(requestLines[0], requestLines[1], requestLines[2])
                    .setHeaders(new HttpHeaders(requestHeaders))
                    .setBody(requestBody)
                    .setUser(user)
                    .build();

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return parsedRequest;
    }

    // Extract file extension from URI
    public static String getFileExtension(String uri) {
        String[] parsedUri = uri.split("\\.");
        return parsedUri[parsedUri.length - 1];
    }

    // Get file path from URI
    public static String getPath(String uri) {
        String extension = getFileExtension(uri);
        if (extension.equals("html")) {
            return "./src/main/resources/templates" + uri;
        }
        return "./src/main/resources/static" + uri;
    }

    // Get Content-Type(MIME-Type) from URI
    public static String getContentType(String uri) {
        String extension = getFileExtension(uri);
        return MIME_CONTENT_TYPE.getOrDefault(extension, DEFAULT_MIME_TYPE);
    }

    // Parsing query string from URI into Map<String, String> Object
    public static Map<String, String> parseQueryString(String uri) {
        Map<String, String> parameters = new HashMap<>();
        String encodedUri = URLDecoder.decode(uri, StandardCharsets.UTF_8);
        try {
            String queryString = encodedUri.split("\\?")[1];
            String[] pairs = queryString.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    parameters.put(keyValue[0], keyValue[1]);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return parameters;
    }

    // Parsing Request Body into Map<String, Stirng> Object
    public static Map<String, String> parseRequestBody(String body) {
        if (body == null) {
            throw new IllegalArgumentException("Require response body");
        }
        Map<String, String> parameters = new HashMap<>();
        String encodedBody = URLDecoder.decode(body, StandardCharsets.UTF_8);
        String[] pairs = encodedBody.split("&");

        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                parameters.put(keyValue[0], keyValue[1]);
            }
        }

        return parameters;
    }

    // Cookie 값의 key, value를 Map<String, String> 형태로 파싱
    public static Map<String, String> parseCookie(String cookie) {
        Map<String, String> parameters = new HashMap<>();
        if (cookie == null) {
            return parameters;
        }

        String[] pairs = cookie.split("; ");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                parameters.put(keyValue[0], keyValue[1]);
            }
        }
        return parameters;
    }
}
