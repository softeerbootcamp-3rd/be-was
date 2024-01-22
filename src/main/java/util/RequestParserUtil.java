package util;

import data.RequestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class RequestParserUtil {

    private static final Logger logger = LoggerFactory.getLogger(RequestParserUtil.class);

    private RequestParserUtil() {}

    public static RequestData parseRequest(BufferedReader br) throws IOException {
        String line = br.readLine();
//        logger.debug("Request line : {}", line);

        if (line == null) {
            throw new IOException("Invalid HTTP request: Request line is null");
        }

        String[] tokens = line.split(" ");

        Map<String, String> headers = parseHeaders(br);

        String requestBody = "";

        if(tokens[0].equals("POST")) {
            int contentLength = Integer.parseInt(headers.getOrDefault("Content-Length", "0"));
            if (contentLength > 0) {
                char[] buffer = new char[contentLength];
                br.read(buffer, 0, contentLength);
                requestBody = new String(buffer);

                logger.debug("requestBody: {}", requestBody);

                return new RequestData(tokens[0], tokens[1], tokens[2], headers, requestBody);
            }
        }

        return new RequestData(tokens[0], tokens[1], tokens[2], headers);
    }

    private static Map<String, String> parseHeaders(BufferedReader br) throws IOException {
        Map<String, String> headers = new HashMap<>();

        String line = br.readLine();

        while (!line.equals("")) {
//            logger.debug("Header: {}", line);
            String[] headerTokens = line.split(": ");
            if (headerTokens.length == 2) {
                headers.put(headerTokens[0], headerTokens[1]);
            }
            line = br.readLine();
        }
        System.out.println();

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
        String userQuery = url.split("\\?")[1];

        // HTTP 요청으로부터 사용자 데이터 추출
        String[] pairs = userQuery.split("&");

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
}
