package common.utils;

import common.http.response.ContentType;
import java.util.HashMap;

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

    public static HashMap<String, String> makeViewHeader(long bodyLength, String target) {
        HashMap<String, String> responseHeader = new HashMap<>();
        responseHeader.put("Content-Length", String.valueOf(bodyLength));
        responseHeader.put("Content-Type", ResponseUtils.getContentType(target));
        return responseHeader;
    }

    public static HashMap<String, String> makeRedirection(String target) {
        HashMap<String, String> header = new HashMap<>();
        header.put("Location", target);
        return header;
    }

    public static HashMap<String, String> makeLoginFailedHeader() {
        HashMap<String, String> header = new HashMap<>();
        header.put("Location", "/user/login_failed.html");
        return header;
    }

    public static HashMap<String, String> makeLoginHeader(String sessionId) {
        HashMap<String, String> header = new HashMap<>();
        header.put("Location", "/index.html");
        header.put("Content-Type", "text/html");
        header.put("Set-Cookie", "sid=" + sessionId + "; Path=/");
        return header;
    }


}
