package util;

import dto.RequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {
    private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);

    public static RequestDto getRequestDto(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

        String line = br.readLine();

        int methodIdx = line.lastIndexOf(" ");
        line = line.substring(0, methodIdx);
        String[] pathWithParams = line.split("\\?");

        RequestDto requestDto = new RequestDto(pathWithParams[0]);

        if (pathWithParams.length == 2) {
            requestDto.setParams(getMapFromQueryStr(pathWithParams[1]));
        }

        while ((line = br.readLine()) != null && !line.equals("")) {
            String[] keyAndValue = line.split(": ");
            requestDto.addHeader(keyAndValue[0], keyAndValue[1]);
        }

        getBody(requestDto, br);

        logger.debug(requestDto.headersToString());

        return requestDto;
    }

    private static void getBody(RequestDto requestDto, BufferedReader br) throws IOException {
        Integer length = requestDto.getContentLength();

        // request 에 body 가 없는 경우
        if (length == null) {
            return;
        }

        char[] body = new char[length];
        br.read(body, 0, length);
        String bodyStr = Arrays.toString(body);
        requestDto.setBody(getMapFromQueryStr(bodyStr));
    }

    private static Map<String, String> getMapFromQueryStr(String str) {
        Map<String, String> result = new HashMap<>();
        for (String param : str.split("&")) {
            String[] keyAndValue = param.split("=");
            result.put(keyAndValue[0], keyAndValue[1]);
        }
        return result;
    }
}

