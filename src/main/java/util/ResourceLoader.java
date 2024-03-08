package util;

import controller.ResourceMapping;
import data.RequestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.UUID;

import static util.RequestParserUtil.getFileExtension;

public class ResourceLoader {

    private static final Logger logger = LoggerFactory.getLogger(ResourceLoader.class);

    public static final String RESOURCE_URL = "src/main/resources";

    private ResourceLoader() {}

    public static byte[] loadResource(String resourcePath, RequestData requestData) throws IOException {
        logger.debug("resourcePath: {}", resourcePath);

        String extension = getFileExtension(resourcePath);
        String directory = ResourceMapping.valueOf(extension.toUpperCase()).getDirectory();

        File file = new File(RESOURCE_URL + directory + resourcePath);

        // 바이트 단위로 읽는 코드
        try (InputStream inputStream = new FileInputStream(file)) {
            // 파일 내용을 바이트 배열로 읽어오기
            byte[] buffer = new byte[(int) file.length()];
            int bytesRead = inputStream.read(buffer);

            // bytesRead가 -1이 아니면 계속 읽기
            while (bytesRead != -1) {
                bytesRead = inputStream.read(buffer);
            }

            if (extension.equals("html")) {
                String modifiedContent = DynamicHtml.modifyHtml(new String(buffer), requestData.isLoggedIn(), requestData);
                return modifiedContent.getBytes();
            }

            return buffer;
        }
    }

    public static String handleFileUpload(Map<String, String> formData, byte[] file) throws IOException {
        // 저장할 디렉토리 경로 설정 (원하는 경로로 변경)
        String uploadDirectory = ResourceMapping.ResourceConstants.UPLOADS_URL;

        // 파일 필드의 이름을 얻음
        String fileName = formData.get("fileName");

        // 파일 내용을 얻음
        byte[] fileContent = file;

        // 파일 아이디 생성 (UUID 사용)
        String fileId = UUID.randomUUID().toString();

        // 파일 확장자 추출 (확장자가 없을 경우 고려 필요)
        String fileExtension = getFileExtension(fileName);

        // 파일을 디스크에 저장
        saveFile(uploadDirectory, fileId, fileExtension, fileContent);

        return fileId;
    }

    private static void saveFile(String directory, String fileId, String fileExtension, byte[] fileContent) throws IOException {
        String filePath = ResourceLoader.RESOURCE_URL + directory + "/" + fileId + "." + fileExtension;

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
            // 파일 저장 실패에 대한 처리
        }
    }
}
