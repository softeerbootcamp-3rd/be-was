package util;

import model.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
    private static final String basePath = "src/main/resources/static";
    private static final String attachmentFolder = "/attachments/";

    static {
        File folder = new File(basePath + attachmentFolder);
        if (!folder.exists() && !folder.mkdirs()) {
            logger.error("Failed to make directory {}", basePath + attachmentFolder);
        }
    }

    public static String saveMultipartFile(MultipartFile multipartFile) throws IOException {
        String filePath = getUniqueFilepath(multipartFile.getFilename());

        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            outputStream.write(multipartFile.getData());
            return extractSavedPath(filePath);
        } catch (IOException e) {
            throw new IOException("Error saving file: " + multipartFile.getFilename(), e);
        }
    }

    private static String getUniqueFilepath(String filename) {
        String filePath = basePath + attachmentFolder + filename;

        // 중복된 파일명이 있으면 (1), (2), (3)과 같은 숫자를 붙임
        int count = 1;
        while (new File(filePath).exists()) {
            int dotIndex = filename.lastIndexOf(".");
            String nameWithoutExtension = (dotIndex == -1) ? filename : filename.substring(0, dotIndex);
            String extension = (dotIndex == -1) ? "" : filename.substring(dotIndex);

            filePath = basePath + attachmentFolder + nameWithoutExtension + " (" + count + ")" + extension;
            count++;
        }
        return filePath;
    }

    private static String extractSavedPath(String fullPath) {
        int startIndex = fullPath.indexOf(basePath);
        if (startIndex == -1)
            return fullPath;
        return fullPath.substring(startIndex + basePath.length());
    }
}
