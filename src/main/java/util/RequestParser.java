package util;

import webserver.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {
    private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);

    public static HttpRequest getHttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

        String line = br.readLine();

        int methodIdx = line.lastIndexOf(" ");
        line = line.substring(0, methodIdx);
        String[] pathWithParams = line.split("\\?");

        HttpRequest httpRequest = new HttpRequest(pathWithParams[0]);

        if (pathWithParams.length == 2) {
            httpRequest.setParams(getMapFromQueryStr(pathWithParams[1]));
        }

        while ((line = br.readLine()) != null && !line.equals("")) {
            String[] keyAndValue = line.split(": ");
             if (keyAndValue.length == 2)
                 httpRequest.addHeader(keyAndValue[0], keyAndValue[1]);
        }

        readCookie(httpRequest);
        readBody(httpRequest, br);

        logger.debug(httpRequest.headersToString());

        return httpRequest;
    }

    private static void readCookie(HttpRequest httpRequest) {
        String cookies;
        if ((cookies = httpRequest.getCookieHeader()) != null) {
            String[] arr = cookies.split(";");
            for (String cookie : arr) {
                String[] keyAndValue = cookie.split("=");
                if (keyAndValue.length == 2) {
                    httpRequest.addCookie(keyAndValue[0], keyAndValue[1]);
                }
            }
        }
    }

    private static void readBody(HttpRequest httpRequest, BufferedReader br) throws IOException {
        Integer length = httpRequest.getContentLength();

        // request 에 body 가 없는 경우
        if (length == null) {
            return;
        }

        char[] body = new char[length];
        br.read(body, 0, length);

        String bodyStr = new String(body);
        httpRequest.setBody(getMapFromQueryStr(bodyStr));
    }

    private static Map<String, String> getMapFromQueryStr(String str) {
        Map<String, String> result = new HashMap<>();

        for (String s : str.split("&")) {
            String[] keyAndValue = s.split("=");
            if (keyAndValue.length == 2)
                result.put(keyAndValue[0], keyAndValue[1]);
        }
        return result;
    }
}
