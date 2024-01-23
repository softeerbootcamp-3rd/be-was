package util;

import constant.FileContentType;
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

    public static String getContentType(String path) throws IOException {
        int extensionPoint = path.lastIndexOf(".");
        return FileContentType.of(path.substring(extensionPoint)).getContentType();
    }
}
