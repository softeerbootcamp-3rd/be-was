package util;

import dto.ResourceDto;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ResourceHandler {

    public static byte[] resolveResource(ResourceDto resource) throws IOException {
        return Files.readAllBytes(new File(getResourcePath(resource.getPath())).toPath());
    }

    private static String getResourcePath(String path) {
        String sourceDirectory = path.contains(".html") ? "./templates" : "./static";
        return ResourceHandler.class.getClassLoader().getResource(sourceDirectory + path).getPath();
    }
}