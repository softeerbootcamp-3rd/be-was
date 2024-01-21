package util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static util.RequestParserUtil.getFileExtension;

public class ResourceLoader {

    public static final String url = "/Users/admin/Softeer/be-was/src/main/resources";

    public static byte[] loadResource(String resourcePath) throws IOException {
        return Files.readAllBytes(Paths.get( url+ "/templates" + resourcePath));
    }

    public static String getResourceType(String targetUrl) {
        String fileOrApi;

        try {
            URI uri = new URI(targetUrl);
            targetUrl = uri.getPath();

            if (!getFileExtension(targetUrl).isEmpty()) fileOrApi = "FILE";
            else fileOrApi = "API";
        } catch (URISyntaxException e) {
            fileOrApi = "UNKNOWN";
        }


        return fileOrApi;
    }
}
