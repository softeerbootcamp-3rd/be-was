package util;

import ch.qos.logback.core.util.FileUtil;
import dto.ResourceDto;
import exception.SourceException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

public class ResourceHandler {

    public static byte[] resolveResource(ResourceDto resource) throws IOException {
        String resourcePath = getResourcePath(resource.getPath());
        FileInputStream fis = new FileInputStream(resourcePath);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int read;
        while ((read = fis.read(buffer)) != -1) {
            bos.write(buffer, 0, read);
        }
        fis.close();
        return bos.toByteArray();
    }

    private static String getResourcePath(String path) {
        String sourceDirectory = path.contains(".html") ? "./templates" : "./static";
        URL resource = ResourceHandler.class.getClassLoader().getResource(sourceDirectory + path);
        if (resource == null) {
            throw new SourceException(ErrorCode.NOT_VALID_PATH);
        }
        return resource.getPath();
    }
}