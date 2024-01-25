package util;

import controller.ResourceMapping;
import data.RequestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

import static util.RequestParserUtil.getFileExtension;

public class ResourceLoader {

    private static final Logger logger = LoggerFactory.getLogger(ResourceLoader.class);

    public static final String url = "/Users/admin/Softeer/be-was/src/main/resources";

    public static byte[] loadResource(String resourcePath, RequestData requestData) throws IOException {
        logger.debug("resourcePath: " + resourcePath);

        String extension = getFileExtension(resourcePath);
        String directory = ResourceMapping.valueOf(extension.toUpperCase()).getDirectory();

        File file = new File(url + directory + resourcePath);

        // 파일을 한번에 읽는 코드
//        try (InputStream inputStream = new FileInputStream(file);
//             BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
//
//            byte[] buffer = new byte[(int) file.length()];
//            bufferedInputStream.read(buffer);
//            return buffer;
//        }

        // 라인 단위로 읽는 코드
//        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//            StringBuilder content = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                content.append(line).append(System.lineSeparator());
//            }
//            content.append("\r\n");
//            return content.toString().getBytes();
//        }

        // 바이트 단위로 읽는 코드
        try (InputStream inputStream = new FileInputStream(file)) {
            // 파일 내용을 바이트 배열로 읽어오기
            byte[] buffer = new byte[(int) file.length()];
            int bytesRead = inputStream.read(buffer);

            // bytesRead가 -1이 아니면 계속 읽기
            while (bytesRead != -1) {
                bytesRead = inputStream.read(buffer);
            }

            if (requestData.getHeaderValue("Cookie") != null && UserService.isLoggedIn(requestData) && extension.equals("html")) {
                String modifiedContent = DynamicHtml.modifyHtml(new String(buffer), true, requestData);
                return modifiedContent.getBytes();
            }

            return buffer;
        }
    }

    public static String getResourceType(String targetUrl) {
        String fileOrApi;

        try {
            URI uri = new URI(targetUrl);
            targetUrl = uri.getPath();

            if (!getFileExtension(targetUrl).isEmpty()) fileOrApi = "FILE";
            else fileOrApi = "API";
        } catch (URISyntaxException e) {
            fileOrApi = "UNKNOWN";
        }


        return fileOrApi;
    }
}
