package util;

import dto.ResourceDto;
import exception.SourceException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

public class ResourceHandler {

    public static byte[] resolveResource(ResourceDto resource) throws IOException {
        String resourcePath = getResourcePath(resource.getPath());
        return Files.readAllBytes(new File(resourcePath).toPath());
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