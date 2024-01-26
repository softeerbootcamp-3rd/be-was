package util;

import db.Database;
import webserver.http.HttpRequest;
import webserver.http.HttpStatus;
import webserver.http.HttpHeader;
import webserver.http.ResponseEntity;

import java.io.*;
import java.util.*;

public class ResourceManager {

    private static final Map<String, String> CONTENT_TYPE_MAP = new HashMap<>();
    private static final String DEFAULT_PATH = "src/main/resources";
    private static final String LOGOUT_BUTTON = "logoutButton";
    private static final String LOGIN_BUTTON = "loginButton";
    private static final String SIGNUP_BUTTON = "singUpButton";

    static {
        CONTENT_TYPE_MAP.put("html", "text/html");
        CONTENT_TYPE_MAP.put("css", "text/css");
        CONTENT_TYPE_MAP.put("js", "application/javascript");
        CONTENT_TYPE_MAP.put("png", "image/png");
        CONTENT_TYPE_MAP.put("jpg", "image/jpeg");
        CONTENT_TYPE_MAP.put("jpeg", "image/jpeg");
        CONTENT_TYPE_MAP.put("gif", "image/gif");
        CONTENT_TYPE_MAP.put("font", "image/svg+xml");
        CONTENT_TYPE_MAP.put("woff", "font/woff");
        CONTENT_TYPE_MAP.put("ttf", "font/ttf");
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

        StringBuilder pathBuilder = new StringBuilder(DEFAULT_PATH);
        if (path.endsWith(".html")) {
            pathBuilder.append("/templates");
        } else {
            pathBuilder.append("/static");
        }
        pathBuilder.append(path);

        File file = new File(pathBuilder.toString());
        if (!file.exists() || !file.isFile())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Map<String, List<String>> header = new HashMap<>();
        header.put(HttpHeader.CONTENT_TYPE, Collections.singletonList(contentType));

        // 동적 HTML : 로그인 상태
        if (path.endsWith(".html") && request.getCookie() != null) {
            String SID = StringParser.getCookieValue(request.getCookie(), "SID");
            if (SessionManager.isValidateSession(SID)) {
                String html = new String(readAllBytes(file));
                String userId = (String) SessionManager.getAttribute(SID, "user");
                // 사용자 이름 표시
                html = changeHTMLButtonStatus(html, SIGNUP_BUTTON, true);
                html = changeHTMLButtonStatus(html, LOGIN_BUTTON, true);
                html = changeHTMLIncludeUserName(html, Database.findUserNameById(userId));
                header.put(HttpHeader.CONTENT_LENGTH, Collections.singletonList(String.valueOf(html.getBytes().length)));
                return new ResponseEntity<>(HttpStatus.OK, header, html);
            }
        } else if (path.endsWith(".html") && request.getCookie() == null) {
            String html = new String(readAllBytes(file));
            // 로그아웃 버튼 숨김
            html = changeHTMLButtonStatus(html, LOGOUT_BUTTON, true);

            header.put(HttpHeader.CONTENT_LENGTH, Collections.singletonList(String.valueOf(html.getBytes().length)));
            return new ResponseEntity<>(HttpStatus.OK, header, html);
        }

        header.put(HttpHeader.CONTENT_LENGTH, Collections.singletonList(String.valueOf(file.length())));
        return new ResponseEntity<>(HttpStatus.OK, header, file);
    }

    public static byte[] readAllBytes(File file) throws IOException {
        byte[] byteArray = null;

        try (FileInputStream fis = new FileInputStream(file)) {
            byteArray = new byte[(int) file.length()];
            fis.read(byteArray);
        }

        return byteArray;
    }

    public static String changeHTMLIncludeUserName(String original, String userName) {
        String changed = "<a id=\"userNameButton\" style=\"display: block;\">" + userName + " 님" + "</a>";
        return original.replace("<a id=\"userNameButton\" style=\"display: none;\"></a>",
                changed);
    }

    public static String changeHTMLButtonStatus(String original, String buttonId, boolean hide) {
        String originalStyle = "none";
        String changedStyle = "block";
        if (hide) {
            originalStyle = "block";
            changedStyle = "none";
        }
        String target = "id=\"" + buttonId + "\" style=\"display: " + originalStyle + ";\"";
        String changed = "id=\"" + buttonId + "\" style=\"display: " + changedStyle + ";\"";
        return original.replace(target, changed);
    }


}
