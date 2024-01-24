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


}
