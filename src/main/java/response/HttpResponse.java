package response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static util.MimeType.getMimeType;

public class HttpResponse {
    private final String contentType;
    private final String redirectUri;
    private final byte[] body;

    public HttpResponse(String contentType, String redirectUri) {
        this.contentType = contentType;
        this.redirectUri = redirectUri;
        this.body = null;
    }

    public HttpResponse(String path) throws IOException {
        this.contentType = getContentType(path);
        this.redirectUri = null;
        this.body = readFileInBytes(Path.of(path));
    }

    public Integer getBodyLength() {
        return body.length;
    }

    public String getContentType() {
        return contentType;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public byte[] getBody() {
        return body;
    }

    private static byte[] readFileInBytes(Path filePath) throws IOException { // 파일을 읽어서 byte[]로 반환
        return Files.readAllBytes(filePath);
    }

    private static String getContentType(String file) { // 파일의 확장자에 따라 Content-Type을 결정
        System.out.println(file);
        String extension = file.substring(file.lastIndexOf("."));
        return getMimeType(extension);
    }
}
