package util;

import data.RequestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;

public class RequestParserUtil {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private RequestParserUtil() {}

    public static RequestData parseRequest(BufferedReader br) throws IOException {
        String line = br.readLine();
        logger.debug("Request line : {}", line);

        if (line == null) {
            throw new IOException("Invalid HTTP request: Request line is null");
        }

        String[] tokens = line.split(" ");

        while(!line.equals("")) { // 헤더의 끝은 빈 공백 문자열이 들어있다.
            line = br.readLine();
            logger.debug("Header : {}", line);
        }

        return new RequestData(tokens[0], tokens[1], tokens[2]);
    }
}
