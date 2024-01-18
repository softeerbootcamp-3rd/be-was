package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

public class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
    private static final String htmlPath = "src/main/resources/templates";
    public static Optional<byte[]> readFile(String uri) {
        try {
            return Optional.of(Files.readAllBytes(new File(htmlPath + uri).toPath()));
        } catch (IOException e) {
            logger.error(e.getMessage());
            return Optional.empty();
        }
    }

//    private static Path getFilePath(String uri) {
//        File file = new File(htmlPath + uri);
//        Path path;
//        if (file.exists() && file.isFile()) {
//            path = file.toPath();
//        }else{
//            path = new File(htmlPath + "/error/404.html").toPath();
//        }
//        return path;
//    }

    private static String getFileType(String uri) {
        int index = uri.lastIndexOf(".");
        return uri.substring(index+1);
    }
}
