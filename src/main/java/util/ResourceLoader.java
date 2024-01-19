package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static util.RequestParserUtil.getFileExtension;

public class ResourceLoader {

    private static final Logger logger = LoggerFactory.getLogger(ResourceLoader.class);

    public static final String url = "/Users/admin/Softeer/be-was/src/main/resources";

    public static byte[] loadResource(String resourcePath) throws IOException {
        logger.debug("resourcePath: " + resourcePath);

        String extension = getFileExtension(resourcePath);

        if(extension.equals("html")) {
            return Files.readAllBytes(Paths.get( url+ "/templates" + resourcePath));
        } else if(extension.equals("css") || extension.equals("js") || extension.equals("woff") || extension.equals("ttf") || extension.equals("ico") || extension.equals("png") || extension.equals("jpg")) {
            return Files.readAllBytes(Paths.get( url+ "/static" + resourcePath));
        }

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
