package utils;

import webserver.http.response.enums.ContentType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtil {
    private static final String BASIC_PATH = "./src/main/resources/templates";
    private static final String STATIC_PATH = "./src/main/resources/static";

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
            return readAllBytes(file);
        }
        return null;
    }

    public static String getFileExtension(String path){
        int lastIndex = path.lastIndexOf(".");
        return (lastIndex != -1) ? path.substring(lastIndex + 1) : "";
    }

    public static byte[] readAllBytes(File file) throws IOException {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fileInputStream.read(data);
            return data;
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }
    }
}
