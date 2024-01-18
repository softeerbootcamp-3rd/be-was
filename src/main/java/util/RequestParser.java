package util;

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

        String[] pathWithParams = line.split("\\?");

        RequestDto requestDto = new RequestDto(pathWithParams[0]);

        if (pathWithParams[1] != null) {
            for (String param : pathWithParams[1].split("&")) {
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
