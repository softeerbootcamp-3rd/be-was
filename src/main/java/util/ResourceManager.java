package util;

import db.Database;
import webserver.http.HttpRequest;
import webserver.http.HttpStatus;
import webserver.http.HttpHeader;
import webserver.http.ResponseEntity;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceManager {

    private static final Map<String, String> CONTENT_TYPE_MAP = new HashMap<>();
    private static final String DEFAULT_PATH = "src/main/resources";

    static {
        CONTENT_TYPE_MAP.put("html", "text/html");
        CONTENT_TYPE_MAP.put("css", "text/css");
        CONTENT_TYPE_MAP.put("js", "application/javascript");
        CONTENT_TYPE_MAP.put("png", "image/png");
        CONTENT_TYPE_MAP.put("jpg", "image/jpeg");
        CONTENT_TYPE_MAP.put("jpeg", "image/jpeg");
        CONTENT_TYPE_MAP.put("gif", "image/gif");
        CONTENT_TYPE_MAP.put("font", "image/svg+xml");
    }

    public static String getContentType(String path) {
        String extension = extractFileExtension(path);
        return CONTENT_TYPE_MAP.getOrDefault(extension, "application/octet-stream");
    }

    private static String extractFileExtension(String filePath) {
        if (filePath == null || filePath.isEmpty()) return "";

        int lastDotIndex = filePath.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filePath.length() - 1) return "";

        return filePath.substring(lastDotIndex + 1);
    }

    public static ResponseEntity handleStaticResource(HttpRequest request) throws IOException {
        String path = request.getPath().equals("/") ? "/index.html" : request.getPath();
        String contentType = getContentType(path);

        byte[] body = null;
        if (path.endsWith(".html")) {
            String filePath = DEFAULT_PATH + "/templates" + path;
            // html일 땐 한 줄씩 String으로 변환해서 처리
            String SID = null;
            if (request.getCookie() != null) {
                SID = StringParser.getCookieValue(request.getCookie(), "SID");
                if (!SessionManager.isValidateSession(SID))
                    SID = null;
            }
            body = readFileLineByLine(filePath, SID);

        } else {
            String filePath = DEFAULT_PATH + "/static" + path;
            // static이면 그냥 byte로 처리
            File file = new File(filePath);
            body = readAllBytes(file);
        }

        if (body == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Map<String, List<String>> header = new HashMap<>();
        header.put(HttpHeader.CONTENT_TYPE, Collections.singletonList(contentType));
        return new ResponseEntity<>(HttpStatus.OK, header, body);
    }

    private static byte[] readFileLineByLine(String filePath, String SID) {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("userNameClass") && SID != null) {
                    sb.append(line + "\n");
                    sb.append("<li>\n"
                            + "<a id=\"userName\">"
                            + Database.findUserNameById((String) SessionManager.getAttribute(SID, "user"))
                            + "님"
                            + "</a>\n"
                            + "</li>");
                    continue;
                }

                if (line.contains("loginButton") && SID != null) {
                    continue;
                }

                if (line.contains("logoutButton") && SID == null) {
                    continue;
                }

                sb.append(line + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return sb.toString().getBytes();
    }

    private static byte[] readAllBytes(File file) throws IOException {
        if (!file.exists())
            return null;

        try (FileInputStream fis = new FileInputStream(file); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            int size = (int) file.length();
            byte[] buffer = new byte[size];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            return bos.toByteArray();
        }
    }

}
