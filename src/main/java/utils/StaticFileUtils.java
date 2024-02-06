package utils;

import http.response.HttpResponse;
import http.enums.Mime;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class StaticFileUtils {
    private static final String TEMPLATE_ROOT = "src/main/resources/templates";
    private static final String STATIC_ROOT = "src/main/resources/static";
    private static final Set<String> STATIC_PATHS = new HashSet<>();
    static {
        // 정적 리소스 경로 초기화
        STATIC_PATHS.add("/css/");
        STATIC_PATHS.add("/js/");
        STATIC_PATHS.add("/fonts/");
        STATIC_PATHS.add("/images/");
        STATIC_PATHS.add("/favicon.ico");
    }

    public static void handleStaticFile(String url, HttpResponse response) throws IOException {
        String filePath = determineFilePath(url);
        String fileExtension = getFileExtension(filePath);
        byte[] body = FileUtils.readFile(filePath);

        if (body == null) {
            response.setStatusCode(404); // 파일이 없을 경우 404 응답
        } else {
            response.setStatusCode(200); // 200 OK 응답
            String mimeType = Mime.convertMime(fileExtension).getMimeType();
            response.setHeader("Content-Type", mimeType); // MIME 타입 설정
            response.setBody(body);
        }
    }

    private static String determineFilePath(String url) {
        if (url.equals("/")) {
            return TEMPLATE_ROOT + "/index.html"; // 루트 URL 요청 시 index.html로 변경
        }
        for (String staticPath : STATIC_PATHS) {
            if (url.startsWith(staticPath) || url.equals(staticPath)) {
                return STATIC_ROOT + url; // 정적 리소스 경로로 시작하는 경우
            }
        }
        return TEMPLATE_ROOT + url; // 그 외의 경우는 'templates' 경로
    }
    private static String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf(".");
        if (lastIndexOfDot == -1) {
            return ""; // 확장자가 없는 경우
        }
        return fileName.substring(lastIndexOfDot + 1);
    }
}
