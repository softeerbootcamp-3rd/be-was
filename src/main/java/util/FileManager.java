package util;

import constant.FilePath;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileManager {
    public static byte[] getFileByPath(String path) throws IOException {
        File file = new File(FilePath.BASE_PATH.getPath() + path);
        if (file.exists()) {
            return Files.readAllBytes(file.toPath());
        }

        return null;
    }
}
