package util;

import constant.FilePath;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileManager {

    public static byte[] getFileByPath(FilePath basePath, String path) throws IOException {
        File file = new File(basePath.getPath() + path);
        if (file.exists()) {
            return Files.readAllBytes(file.toPath());
        }

        return null;
    }

    public static String getContentType(String path) throws IOException {
        return Files.probeContentType(Paths.get(path));
    }
}
