package util;

import constant.FilePath;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileManager {

    public static byte[] getFileByPath(FilePath basePath, String path) throws IOException {
        File file = new File(basePath.getPath() + path);
        if (file.exists()) {
            return Files.readAllBytes(file.toPath());
        }

        return null;
    }

    public static String getFileExtension(String path) {
        int i = path.lastIndexOf('.');
        return path.substring(i+1);
    }
}
