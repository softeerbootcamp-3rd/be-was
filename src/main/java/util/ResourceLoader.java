package util;

import controller.ResourcePathMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static util.RequestParserUtil.getFileExtension;

public class ResourceLoader {

    private static final Logger logger = LoggerFactory.getLogger(ResourceLoader.class);

    public static final String url = "/Users/admin/Softeer/be-was/src/main/resources";

    public static byte[] loadResource(String resourcePath) throws IOException {
        logger.debug("resourcePath: " + resourcePath);

        String extension = getFileExtension(resourcePath);
        String directory = ResourcePathMapping.getDirectory(extension);

        File file = new File(url + directory + resourcePath);

        try (InputStream inputStream = new FileInputStream(file);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {

            byte[] buffer = new byte[(int) file.length()];
            bufferedInputStream.read(buffer);
            return buffer;
        }
//         라인 단위로 읽는 코드
//        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//            StringBuilder content = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                content.append(line).append(System.lineSeparator());
//            }
//            return content.toString().getBytes(StandardCharsets.UTF_8);
//        }
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
