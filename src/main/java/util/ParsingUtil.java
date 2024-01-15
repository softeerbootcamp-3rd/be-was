package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ParsingUtil {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);


    //url에 해당하는 파일들을 읽어서 하나의 byte[]로 반환
    public static byte[] combineResources(ArrayList<String> urls) throws IOException {
        ByteArrayOutputStream combinedStream = new ByteArrayOutputStream();

        //url에 해당하는 파일을 읽어서 byte[]로 반환
        for (String url : urls) {
            byte[] resourceContent = getResource(url);
            combinedStream.write(resourceContent);
        }

        return combinedStream.toByteArray();
    }


    //url에 해당하는 파일을 읽어서 byte[]로 반환
    public static byte[] getResource(String path) throws IOException {

        Path filePath;

        //path에 해당하는 파일을 읽어서 byte[]로 반환
        if (path.endsWith(".html")) {
            filePath = Paths.get("src/main/resources/templates", path);
        } else {
            filePath = Paths.get("src/main/resources/static", path);
        }

        try {
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            logger.error("Error reading resource: {}", path);
            throw e;
        }
    }
}
