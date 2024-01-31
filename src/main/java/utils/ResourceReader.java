package utils;

import webserver.http.response.enums.ContentType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ResourceReader {
    private static final String BASIC_PATH = "./src/main/resources/templates";
    private static final String STATIC_PATH = "./src/main/resources/static";

    private static final ResourceReader instance = new ResourceReader();

    private ResourceReader(){};

    public static ResourceReader getInstance(){
        return instance;
    }

    public byte[] getFileContents(String path) throws IOException {
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

    public String getFileTemplate(String path) throws IOException {
        byte[] fileContents = getFileContents(path);
        return new String(fileContents);
    }

    public String getFileExtension(String path){
        int lastIndex = path.lastIndexOf(".");
        return (lastIndex != -1) ? path.substring(lastIndex + 1) : "";
    }

    private byte[] readAllBytes(File file) throws IOException {
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
