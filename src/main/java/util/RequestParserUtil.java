package util;

import controller.HttpMethod;
import controller.HttpStatusCode;
import data.RequestData;
import data.Response;
import model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class RequestParserUtil {

    private static final Logger logger = LoggerFactory.getLogger(RequestParserUtil.class);

    private RequestParserUtil() {}

    public static RequestData parseRequest(InputStream in) throws IOException {
        ByteReader byteReader = new ByteReader(in);

        String line = byteReader.readLine();

        if (line == null) {
            throw new IOException("Invalid HTTP request: Request line is null");
        }

        String[] tokens = line.split(" ");

        Map<String, String> headers = parseHeaders(byteReader);

        // 요청의 로그인 여부 판단
        boolean isLoggedIn = false;
        if (headers.get("Cookie") != null) {
            isLoggedIn = UserService.isLoggedIn(headers.get("Cookie"));
        }

        String requestBody = "";

        if(tokens[0].equals("POST")) {
            int contentLength = Integer.parseInt(headers.getOrDefault("Content-Length", "0"));
            if (contentLength > 0) {
                if (headers.containsKey("Content-Type") && headers.get("Content-Type").startsWith("multipart/form-data")) {
                    // 멀티파트 데이터인 경우
                    String boundary = extractBoundary(headers.get("Content-Type"));
                    Map<String, String> formData = parseMultipartRequest(in, boundary, contentLength);
                    Post postData;
                    if(formData.containsKey("fileId")){
                        postData = new Post(formData.get("writer"), formData.get("title"), formData.get("contents"), formData.get("fileId"), formData.get("fileExtension"), null);
                    } else {
                        postData = new Post(formData.get("writer"), formData.get("title"), formData.get("contents"), "-1", "", null);
                    }
                    return new RequestData(HttpMethod.POST, tokens[1], tokens[2], headers, postData, isLoggedIn);
                } else {
                    // 일반적인 POST 요청인 경우
                    byte[] buffer = new byte[contentLength];
                    byteReader.read(buffer);
                    requestBody = new String(buffer);
                    logger.debug("requestBody: {}", requestBody);

                    return new RequestData(HttpMethod.POST, tokens[1], tokens[2], headers, requestBody, isLoggedIn);
                }            }
        } else if (tokens[0].equals("GET")) {
            // GET 요청의 경우 URL에서 쿼리 매개변수를 추출
            int queryIndex = tokens[1].indexOf('?');
            if (queryIndex != -1) {
                String path = tokens[1].substring(0, queryIndex);
                String queryString = tokens[1].substring(queryIndex + 1);

                return new RequestData(HttpMethod.GET, path, tokens[2], headers, queryString, isLoggedIn);
            } else {
                return new RequestData(HttpMethod.GET, tokens[1], tokens[2], headers, isLoggedIn);
            }
        }

        return new RequestData(HttpMethod.valueOf(tokens[0]), tokens[1], tokens[2], headers, isLoggedIn);
    }

    private static Map<String, String> parseHeaders(ByteReader br) throws IOException {
        Map<String, String> headers = new HashMap<>();

        String line = br.readLine();

        while (!line.equals("")) {
            // Trailing white space 고려하여 ":"으로 split() 후 trim() 호출
            int colonIndex = line.indexOf(':');
            if (colonIndex != -1) {
                String headerName = line.substring(0, colonIndex).trim();
                String headerValue = line.substring(colonIndex + 1).trim();
                headers.put(headerName, headerValue);
            }
            line = br.readLine();
        }

        return headers;
    }

    public static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1);
        } else {
            return ""; // 확장자가 없는 경우
        }
    }

    public static Map<String, String> parseUserRegisterQuery(String url) {
        // HTTP 요청으로부터 사용자 데이터 추출
        String[] pairs = url.split("&");

        Map<String, String> userProps = new HashMap<>();
        for (String pair : pairs) {
            String[] splitPair = pair.split("=");
            if (splitPair.length == 2) {
                String key = splitPair[0];
                String val;
                val = URLDecoder.decode(splitPair[1]);
                userProps.put(key, val);
            }
        }

        return userProps;
    }

    private static String extractBoundary(String contentType) {
        // Content-Type 헤더에서 boundary 추출
        // Content-Type: multipart/form-data; boundary=----WebKitFormBoundaryoHB4xN0eAimfovj5
        String[] parts = contentType.split("; ");
        for (String part : parts) {
            if (part.startsWith("boundary=")) {
                return part.substring("boundary=".length());
            }
        }
        return null;
    }

    private static Map<String, String> parseMultipartRequest(InputStream in, String boundary, int contentLength) throws IOException {
        Map<String, String> formData = new HashMap<>();

        // 읽은 바이트 수를 저장하는 변수 추가
        int bytesRead = 0;

        ByteReader byteReader = new ByteReader(in);

        String line;
        while ((line = byteReader.readLine()) != null) {
            bytesRead += line.length() + 1; // 개행 문자도 포함

            // 멀티파트 경계 처리
            if (line.startsWith("--" + boundary)) {
                continue;
            }

            // Content-Disposition 헤더 처리
            if (line.startsWith("Content-Disposition:")) {
                String[] dispositionParts = line.split("; ");
                String fieldName = "";
                String fileName = "";

                for (String part : dispositionParts) {
                    if (part.startsWith("name=")) {
                        fieldName = part.substring(6, part.length() - 1);
                    } else if (part.startsWith("filename=")) {
                        fileName = part.substring(10, part.length() - 1);
                    }
                }

                if (fieldName.equals("file") && fileName.isEmpty()) {
                    break;
                }

                if (!fileName.isEmpty()) {
                    // 파일일 경우에 대한 처리
                    byte[] fileContent = byteReader.readMultipartFileContent(byteReader, contentLength, boundary);
                    formData.put("fileName", fileName);
                    formData.put("fileExtension", getFileExtension(fileName));
                    formData.put("fileId", ResourceLoader.handleFileUpload(formData, fileContent));
                    break;
                } else {
                    // 일반 텍스트 필드 처리
                    StringBuilder fieldValue = new StringBuilder();
                    while ((line = byteReader.readLine()) != null && !line.startsWith("--" + boundary)) {
                        fieldValue.append(line);
                    }
                    formData.put(fieldName, fieldValue.toString());
                }
            }
        }

        return formData;
    }
}
