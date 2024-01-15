package logger;

import java.io.IOException;
import java.io.InputStream;
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
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
    }

    // header 출력 부
    public static void printHeader(HttpRequestHeader httpRequestHeader) throws IOException {
        logger.debug("Headers : \n{}", httpRequestHeader.toString());
    }

    // body 출력 부
    public void printBody() {
        // TODO: body 출력 부
    }
}
