package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileReader {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final String DATA = "src/main/resources";
    private static final String STATIC = "src/main/resources/static";
    private static final String TEMPLATE = "src/main/resources/templates";

    public static String getBasePath(String requestURL) {
        if (requestURL.startsWith("/data/")) {
            return DATA;
        }
        if(requestURL.startsWith("/css/") || requestURL.startsWith("/fonts/") ||requestURL.startsWith("/images/") ||
                requestURL.startsWith("/js/") || requestURL.contains(".ico"))
            return STATIC;

        if (requestURL.contains(".html") || requestURL.contains(".xml")) {
            return TEMPLATE;
        }

        return "";
    }

    public static byte[] readFile(File file) {
        try {
            // 파일을 읽어오기 위한 FileInputStream 생성
            FileInputStream fis = new FileInputStream(file);

            // 파일 내용을 저장할 ByteArrayOutputStream 생성
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            // 파일 내용을 읽어와서 ByteArrayOutputStream에 쓰기
            // 고민사항 : file.length()는 long 형 인데 이를 int 형으로 형변환하면 나중에 큰 파일을 읽어온다고 했을 때 정보가 누락될 수 있음...
            byte[] buffer = new byte[(int) file.length()];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            // FileInputStream과 ByteArrayOutputStream 닫기
            fis.close();
            bos.close();

            // byte[]로 변환
            return bos.toByteArray();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return new byte[0];
    }
}
