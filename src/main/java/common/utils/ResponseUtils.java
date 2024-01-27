package common.utils;

import common.http.response.ContentType;
import java.util.HashMap;
import java.util.Map;

public class ResponseUtils {

    public static String getContentType(String target) {
        String fileExtension = getFileExtension(target);

        ContentType contentType = ContentType.getByFileExtension(fileExtension);
        return contentType.getValue();
    }

    public static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    public static Map<String, String> makeViewHeader(long bodyLength, String target) {
        Map<String, String> responseHeader = new HashMap<>();
        responseHeader.put("Content-Length", String.valueOf(bodyLength));
        responseHeader.put("Content-Type", ResponseUtils.getContentType(target));
        return responseHeader;
    }

    public static Map<String, String> makeRedirection(String target) {
        Map<String, String> header = new HashMap<>();
        header.put("Location", target);
        return header;
    }

    public static Map<String, String> makeLoginFailedHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("Location", "/user/login_failed.html");
        return header;
    }

    public static Map<String, String> makeLoginHeader(String sessionId) {
        Map<String, String> header = new HashMap<>();
        header.put("Location", "/index.html");
        header.put("Content-Type", "text/html");
        header.put("Set-Cookie", "sid=" + sessionId + "; Path=/; Max-Age=86400");
        return header;
    }


    public static Map<String, String> makeJsonResponseHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        return header;
    }
}
