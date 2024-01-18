package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ResourceLoader {

    private static final String url = "/Users/admin/Softeer/be-was/src/main/resources";

    public static byte[] loadResource(String resourcePath) throws IOException {
        return Files.readAllBytes(Paths.get( url+ "/templates" + resourcePath));
    }
}
