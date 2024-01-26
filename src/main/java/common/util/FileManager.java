package common.util;

import java.io.FileInputStream;
import java.io.IOException;

public class FileManager {

    private static final String RESOURCES_PATH = "src/main/resources/";

    public static byte[] getFile(String url, String contentType) throws IOException {
        String path = RESOURCES_PATH;
        if (contentType.equals("text/html")) {
            path += "templates";
        } else {
            path += "static";
        }
        path += url;
        return fileToByte(path);
    }

    private static byte[] fileToByte(String path) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            return fileInputStream.readAllBytes();
        }
    }
}
