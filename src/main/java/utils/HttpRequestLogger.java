package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class HttpRequestLogger {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestLogger.class);

    public static void logHttpRequest(InputStream in){
        try{
            RequestParser.parseHeaders(in);
            logger.debug("HEADER> METHOD: {}", RequestParser.getMethod());
            logger.debug("HEADER> HOST: {}", RequestParser.getHost());
            logger.debug("HEADER> PATH: {}", RequestParser.getPath());
            logger.debug("HEADER> ACCEPT: {}", RequestParser.getAccept());
            logger.debug("HEADER> USER-AGENT: {}", RequestParser.getUserAgent());
            logger.debug("HEADER> COOKIE: {}", RequestParser.getCookie());
        } catch (IOException e) {
            logger.error("Error logging request: {}", e.getMessage());
        }
    }
}
