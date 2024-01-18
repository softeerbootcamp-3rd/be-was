package webserver;

import dto.RequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RequestParser {
    private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);

    public static RequestDto getRequestDto(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

        String line = br.readLine();
        // 이 부분 스플릿 순서에 대해 다시 생각 -> ? 먼저 하면 나중에 문자열 연산 필요 없음
        String[] methodAndPath = line.split(" ");
        String[] pathWithParams = methodAndPath[1].split("\\?");
        String params = (pathWithParams.length == 1) ? null : pathWithParams[1];

        RequestDto requestDto = new RequestDto(methodAndPath[0], pathWithParams[0]);

        if (params != null) {
            for (String param : params.split("&")) {
                String[] keyAndValue = param.split("=");
                requestDto.addParam(keyAndValue[0], keyAndValue[1]);
            }
        }

        while ((line = br.readLine()) != null && !line.equals("")) {
            String[] keyAndValue = line.split(": ");
            requestDto.addHeader(keyAndValue[0], keyAndValue[1]);
        }

        logger.debug(requestDto.headersToString());

        return requestDto;
    }
}
