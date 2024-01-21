package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileLoader {
    private static final String HTML_PATH = "src/main/resources/templates";

    public static byte[] loadFileContent(String requestPath) throws IOException {
        return Files.readAllBytes(new File(HTML_PATH + requestPath).toPath());
    }
}
