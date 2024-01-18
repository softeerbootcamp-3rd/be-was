package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileUtil {
    public static final String BASIC_PATH = "./src/main/resources/templates";

    public static byte[] getFileContents(String path) throws IOException {
        File file = new File(BASIC_PATH + path);
        if (file.exists() && !"/".equals(path)) {
            return Files.readAllBytes(file.toPath());
        }
        return null;
    }
}
