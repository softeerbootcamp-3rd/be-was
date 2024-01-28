package util.web;

import constant.MimeType;
import exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ResourceLoader {

    private static final Logger logger = LoggerFactory.getLogger(ResourceLoader.class);

    public static String getMimeType(String path) {
        String extension = extractFileExtension(path);
        return MimeType.getByExtension(extension).getMimeType();
    }

    private static String extractFileExtension(String filePath) {
        if (filePath == null || filePath.isEmpty()) return "";

        int lastDotIndex = filePath.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filePath.length() - 1) return "";

        return filePath.substring(lastDotIndex + 1);
    }

    public static byte[] getFileContent(String path) throws IOException {
        String basePath = "src/main/resources/templates";
        if (path.startsWith("/css/") || path.startsWith("/fonts/")
                || path.startsWith("/images/") || path.startsWith("/js/"))
            basePath = "src/main/resources/static";

        File file = new File(basePath + path);
        if (!file.exists() || !file.isFile())
            throw new ResourceNotFoundException(path);
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] content = new byte[(int) file.length()];
            if (fis.read(content) == -1)
                return new byte[0];
            return content;
        }
    }
}
