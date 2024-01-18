package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ResponseBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ResponseBuilder.class);

    public static void response(String status, String route, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        if (status.equals("200")) {
            byte[] body = getContent(route); // 해당하는 경로의 파일을 읽고 byte[]로 반환
            String contentType = getContentType(route); // 파일의 확장자에 따라 Content-Type을 결정
            response200Header(dos, body.length, contentType);
            responseBody(dos, body);
        }
        else if (status.equals("302")) {
            response302Header(dos, route);
        }
    }


    private static void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static void response302Header(DataOutputStream dos, String location) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + location + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }


    private static void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush(); //
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
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
        String contentType = "";

        switch (file.substring(file.lastIndexOf("."))) {
            case ".html":
                contentType = ExtensionType.HTML.getExtension();
                break;
            case ".css":
                contentType = ExtensionType.CSS.getExtension();
                break;
            case ".js":
                contentType = ExtensionType.JS.getExtension();
                break;
            case ".png":
                contentType = ExtensionType.PNG.getExtension();
                break;
            case ".ico":
                contentType = ExtensionType.ICO.getExtension();
                break;
            case ".svg":
                contentType = ExtensionType.SVG.getExtension();
                break;
            case ".woff":
                contentType = ExtensionType.WOFF.getExtension();
                break;
            case ".ttf":
                contentType = ExtensionType.TTF.getExtension();
                break;
            case ".eot":
                contentType = ExtensionType.EOT.getExtension();
                break;
            case ".woff2":
                contentType = ExtensionType.WOFF2.getExtension();
                break;
            default:
                contentType = ExtensionType.HTML.getExtension();
                break;
        }
        return contentType;
    }
}
