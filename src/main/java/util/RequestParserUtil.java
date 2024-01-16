package util;

import data.RequestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestParserUtil {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private RequestParserUtil() {}

    public static RequestData parseRequest(BufferedReader br) throws IOException {
        String line = br.readLine();
//        logger.debug("Request line : {}", line);

        if (line == null) {
            throw new IOException("Invalid HTTP request: Request line is null");
        }

        String[] tokens = line.split(" ");

        Map<String, String> headers = parseHeaders(br);

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
}
