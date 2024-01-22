package common.logger;

import common.http.request.HttpRequest;
import java.net.Socket;
import org.slf4j.LoggerFactory;

public class CustomLogger {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CustomLogger.class);

    public static void printInfo(String message) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stackTrace[2];
        logger.info("[{}] {}", caller, message);
    }

    public static void printError(Exception e) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stackTrace[2];
        logger.error("[{}] {}", caller, e.getMessage());
    }

    public static void printIPAndPort(Socket connection) {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}",
            connection.getInetAddress(), connection.getPort());
    }

    public static void printRequest(HttpRequest httpRequest) {
        logger.debug("StartLines : \n{}", httpRequest.getHttpRequestStartLine().toString());
        logger.debug("Headers : \n{}", httpRequest.getHttpRequestHeader().toString());
        logger.debug("Body : \n{}", httpRequest.getHttpRequestBody().toString());
    }

}
