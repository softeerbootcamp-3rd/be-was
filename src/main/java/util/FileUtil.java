package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public static byte[] getBody(String path) throws IOException { // path에 해당하는 파일을 읽어서 byte[]로 반환

        Path filePath;

        if (path.endsWith(".html")) {
            filePath = Paths.get("src/main/resources/templates", path);
        } else {
            filePath = Paths.get("src/main/resources/static", path);
        }

        try {
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            logger.error("Error reading resource: {}", path);
            throw e;
        }
    }

    public static String getContentType(String file) { // 파일의 확장자에 따라 Content-Type을 결정
        String contentType = "text/html";
        if (file.endsWith(".css")) {
            contentType = "text/css";
        }
        else if (file.endsWith(".js")) {
            contentType = "application/javascript";
        }
        return contentType;
    }
}
