package utils;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
    public static File getFileFromUrl(String url) throws IOException {
        Path source = Paths.get(url);
        String contentType = getContentType(source);

        if (contentType.equals("text/html")) {
            return new File("src/main/resources/templates" + url);
        } else {
            return new File("src/main/resources/static" + url);
        }
    }

    public static String getContentType(Path path) throws IOException {
        String contentType = Files.probeContentType(path);
        if (contentType == null) {
            if (path.toString().endsWith(".woff"))
                return "font/woff";
            return "application/octet-stream";
        }
        return contentType;
    }
}
