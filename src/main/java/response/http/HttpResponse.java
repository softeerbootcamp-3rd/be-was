package response.http;

import util.StatusCode;

import java.io.*;

import static util.MimeType.getMimeType;

public class HttpResponse {
    private final StatusLine statusLine;
    private final String contentType;
    private final String sid;
    private final String redirectUri;
    private final byte[] body;

    public HttpResponse(StatusCode statusCode, String contentType, String redirectUri, String sid) {
        this.statusLine = new StatusLine(statusCode);
        this.contentType = contentType;
        this.sid = sid;
        this.redirectUri = redirectUri;
        this.body = null;
    }

    public HttpResponse(StatusCode statusCode, String filePath, StringBuilder builder) throws IOException {
        this.statusLine = new StatusLine(statusCode);
        this.contentType = getContentType(filePath);
        this.sid = null;
        this.redirectUri = null;
        this.body = readFileInBytesAndReplace(filePath, builder);
    }

    public HttpResponse(StatusCode statusCode, String filePath) throws IOException {
        this.statusLine = new StatusLine(statusCode);
        this.contentType = getContentType(filePath);
        this.sid = null;
        this.redirectUri = null;
        this.body = readFileInBytes(filePath);
    }

    public HttpResponse(StatusCode statusCode) {
        this.statusLine = new StatusLine(statusCode);
        this.contentType = null;
        this.sid = null;
        this.redirectUri = null;
        this.body = "File Not Found".getBytes();
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

    public Integer getStatusCode() {
        return statusLine.getStatusCode();
    }

    public String getStatusCodeAndReasonPhrase() {
        return statusLine.getStatusCode() + " " + statusLine.getReasonPhrase();
    }

    public String getSid() {
        return sid;
    }

    public StatusLine getStatusLine() {
        return statusLine;
    }

    public static byte[] readFileInBytesAndReplace(String filePath, StringBuilder builder) throws IOException { // 파일을 읽어서 byte[]로 변환 후 {{userList}}를 builder로 치환
        File file = new File(filePath);

        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);

        byte[] buffer = new byte[(int) file.length()];
        bis.read(buffer);

        String fileContent = new String(buffer);

        StringBuilder replacedBuffer = new StringBuilder(fileContent);
        replacedBuffer.replace(replacedBuffer.indexOf("{{userList}}"),
                replacedBuffer.indexOf("{{userList}}") + "{{userList}}".length(),
                builder.toString());

        return replacedBuffer.toString().getBytes();
    }

    public static byte[] readFileInBytes(String filePath) throws IOException { // 파일을 읽어서 byte[]로 반환
        File file = new File(filePath);

        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);

        byte[] buffer = new byte[(int) file.length()];
        bis.read(buffer);

        return buffer;
    }

    private static String getContentType(String file) { // 파일의 확장자에 따라 Content-Type을 결정
        System.out.println(file);
        String extension = file.substring(file.lastIndexOf("."));
        return getMimeType(extension);
    }
}
