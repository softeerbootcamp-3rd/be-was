package common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static webserver.RequestHandler.logger;

public class FileManager {

    private static final String RESOURCES_PATH = "src/main/resources/";

    public static byte[] getFile(String url, String contentType) throws IOException, FileNotFoundException {
        String path = RESOURCES_PATH;
        if (contentType.equals("text/html")) {
            path += "templates";
        } else {
            path += "static";
        }
        path += url;

        File file = new File(path);
        if (!file.exists()) {
            logger.debug("-------------{}", "FileNotFoundException.class");
            throw new FileNotFoundException();
        }
        return fileToByte(path);
    }

    private static byte[] fileToByte(String path) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            return fileInputStream.readAllBytes();
        }
    }
}
