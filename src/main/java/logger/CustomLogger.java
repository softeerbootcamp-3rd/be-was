package logger;

import http.request.HttpRequest;
import java.net.Socket;
import org.slf4j.LoggerFactory;

public class CustomLogger {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CustomLogger.class);

    public static void printInfo(String message) {
        logger.info(message);
    }

    public static void printError(Exception e) {
        logger.error(e.getMessage());
    }

    // IP, Port 출력 부
    public static void printIPAndPort(Socket connection) {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}",
            connection.getInetAddress(), connection.getPort());
    }

    // request 출력 부
    public static void printRequest(HttpRequest httpRequest) {
        logger.debug("StartLines : \n{}", httpRequest.getHttpRequestStartLine().toString());
        logger.debug("Headers : \n{}", httpRequest.getHttpRequestHeader().toString());
        logger.debug("Body : \n{}", "Not Implemented");
    }

}
