package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileReader {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final String STATIC = "src/main/resources/static";
    private static final String TEMPLATE = "src/main/resources/templates";

    public static String getFileExtension(Path filePath) {
        // 파일명을 가져옴
        String fileName = filePath.getFileName().toString();

        // 파일명에서 마지막 점(.)의 인덱스를 찾음
        int lastDotIndex = fileName.lastIndexOf('.');

        // 마지막 점(.)이 없거나 파일명이 . 으로 시작하는 경우 확장자가 없음
        if (lastDotIndex == -1 || lastDotIndex == 0 || lastDotIndex == fileName.length() - 1) {
            return ""; // 확장자 없음
        }

        // 확장자를 추출하여 반환
        return fileName.substring(lastDotIndex + 1);
    }


    public static String getBasePath(String requestURL) {
        if(requestURL.contains("/css/") || requestURL.contains("/fonts/") ||requestURL.contains("/images/") ||
                requestURL.contains("/js/") || requestURL.contains(".ico"))
            return STATIC;

        return TEMPLATE;
    }

    public static byte[] readFile(String path) {
        String basePath = getBasePath(path);
        try {
            return Files.readAllBytes(new File(basePath + path).toPath());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return new byte[0];
    }
}
