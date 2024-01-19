package response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static util.MIMEType.getMIMEType;

public class HttpResponse {
    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);
    private final String contentType;
    private final String redirectUrl;
    private final byte[] body;

    public HttpResponse(String path, String redirectUrl) {
        this.contentType = getContentType(path);
        this.redirectUrl = redirectUrl;
        this.body = null;
    }

    public HttpResponse(String path) throws IOException {
        this.contentType = getContentType(path);
        this.redirectUrl = null;
        this.body = getContent(path);
    }

    public Integer getBodyLength() {
        return body.length;
    }

    public String getContentType() {
        return contentType;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public byte[] getBody() {
        return body;
    }

    private static byte[] getContent(String path) throws IOException { // path에 해당하는 파일을 읽어서 byte[]로 반환

        Path filePath;

        if (path.endsWith(".html")) {
            filePath = Paths.get("src/main/resources/templates", path);
        } else {
            filePath = Paths.get("src/main/resources/static", path);
        }

        try {
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            logger.error("Error reading resource: {}", path);
            throw e;
        }
    }

    private static String getContentType(String file) { // 파일의 확장자에 따라 Content-Type을 결정
        String extension = file.substring(file.lastIndexOf("."));
        return getMIMEType(extension);
    }
}
