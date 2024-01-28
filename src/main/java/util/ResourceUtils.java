package util;

import java.io.File;
import java.io.IOException;

public class ResourceUtils {
    private static final String TEMPLATES = "./src/main/resources/templates";
    private static final String STATIC = "./src/main/resources/static";

    public static byte[] getStaticResource(String path) throws IOException {
        return Files.readAllBytes(getFileFromPath(path));
    }

    private static File getFileFromPath(String path) {
        if (path.equals("/"))
            path = "/index.html";

        String extension = getExtension(path);

        File file;
        if (extension.equals("html")) {
            file = new File(TEMPLATES + path);
            if (!file.exists())
                file = new File(STATIC + path);
        } else
            file = new File(STATIC + path);

        return file;
    }

    public static String getExtension(String path) {
        String[] tokens = path.split("\\.");
        return tokens[tokens.length - 1];
    }

    public static String getLastPath(String path) {
        if (path == null || "/".equals(path))
            return path;
        String[] tokens = path.split("/");
        return tokens[tokens.length - 1];
    }
}
