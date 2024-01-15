package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class WebUtil {
    private static final Logger logger = LoggerFactory.getLogger(WebUtil.class);
    public static HttpRequestParam HttpRequestParse(InputStream request) {
        BufferedReader br = new BufferedReader(new InputStreamReader(request));
        String line = null;
        HttpRequestParam parsedRequest = null;
        try {
            line = br.readLine();
            String[] tokens = line.split(" ");
            parsedRequest = new HttpRequestParam(tokens[0], tokens[1]);
            return parsedRequest;
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return parsedRequest;
    }
}
