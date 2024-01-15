package webserver;

import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class WebUtil {
    public static HttpRequestParam HttpRequestParse(InputStream request, Logger logger) {
        BufferedReader br = new BufferedReader(new InputStreamReader(request));
        String line = null;
        HttpRequestParam parsedRequest = null;
        try {
            line = br.readLine();
            String[] tokens = line.split(" ");
            parsedRequest = new HttpRequestParam(tokens[0], tokens[1]);
            logger.debug("HTTP Request Header >> " + line);
//            while (line != null && !line.isEmpty()) {
//                logger.debug(line);
//                line = br.readLine();
//            }
            return parsedRequest;
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return parsedRequest;
    }
}
