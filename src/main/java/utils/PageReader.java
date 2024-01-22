package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import model.Response;

public class PageReader {

    /**
     * 페이지 파일을 불러오는 함수입니다.
     *
     * <p> 요청한 파일을 찾을 수 있는 경우 200 응답을 반환하며, 요청한 파일을 찾을 수 없는 경우 404 응답을 반환합니다.
     *
     * @param url 요청 타겟
     * @param filePath 파일 경로
     * @return 파일을 불러온 결과를 담은 응답
     */
    public static Response getPage(String url, String filePath) {
        File file = new File(filePath);

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] body = new byte[(int) file.length()];
            fis.read(body);
            return new Response(200, url, body);
        } catch (IOException e) {
            byte[] body = "404 Not Found".getBytes();
            return new Response(404, body);
        }
    }
}
