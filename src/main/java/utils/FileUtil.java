package utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

public class FileUtil {
    private enum ContentType {
        HTML(".html", "text/html"),
        CSS(".css", "text/css"),
        JS(".js", "text/javascript"),
        TTF(".ttf", "font/ttf"),
        WOFF(".woff", "font/woff"),
        ICO(".ico", "image/vnd.microsoft.icon"),
        ;

        private final String extension;
        private final String mimeType;

        ContentType(String extension, String mimeType) {
            this.extension = extension;
            this.mimeType = mimeType;
        }

        public String getExtension() {
            return extension;
        }

        public String getMimeType() {
            return mimeType;
        }
    }


    public static File getFileFromUrl(String url) {
        String contentType = getContentType(url);

        if (contentType.equals("text/html")) {
            return new File("src/main/resources/templates" + url);
        } else {
            return new File("src/main/resources/static" + url);
        }
    }

    public static String getContentType(String url) {
        String urlExtension;
        ContentType contentType;

        if (url.lastIndexOf('.') > 0) {
            urlExtension = url.substring(url.lastIndexOf('.'));
        } else {
            urlExtension = "";
        }

        try {
            contentType = Stream.of(ContentType.values())
                    .filter(type -> type.getExtension().equals(urlExtension))
                    .findFirst().orElseThrow();
        } catch (NoSuchElementException e) {
            return "application/octet-stream";
        }

        return contentType.getMimeType();
    }

    public static byte[] readFile(File file) throws IOException {
        if (file.length() > Integer.MAX_VALUE)
            throw new IOException("File is too large");

        byte[] result = new byte[(int) file.length()];
        int offset = 0;
        int numRead = 0;
        try (InputStream is = new FileInputStream(file)) {
            while (offset < result.length && (numRead = is.read(result, offset, result.length - offset)) >= 0) {
                offset += numRead;
            }
        }

        return result;
    }
}
