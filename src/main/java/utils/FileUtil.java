package utils;

import webserver.http.response.enums.ContentType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileUtil {
    public static final String BASIC_PATH = "./src/main/resources/templates";
    public static final String STATIC_PATH = "./src/main/resources/static";

    public static byte[] getFileContents(String path) throws IOException {
        String fileExtension = getFileExtension(path);
        ContentType contentType = ContentType.toContentType(fileExtension);

        String filePath;
        if(contentType == ContentType.HTML){
            filePath = BASIC_PATH + path;
        } else{
            filePath = STATIC_PATH + path;
        }

        File file = new File(filePath);
        if (file.exists() && !"/".equals(path)) {
            return Files.readAllBytes(file.toPath());
        }
        return null;
    }

    public static String getFileExtension(String path){
        int lastIndex = path.lastIndexOf(".");
        return (lastIndex != -1) ? path.substring(lastIndex + 1) : "";
    }
}
