package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PageReader {

    /**
     * 페이지 파일을 불러오는 함수입니다.
     *
     * <p> 요청한 파일을 찾을 수 있는 경우 바이트 배열을 반환하고, 찾을 수 없는 경우 오류를 발생시킵니다.
     *
     * @param filePath 파일 경로
     * @return 읽어낸 파일 바이트 배열
     * @throws IOException 파일을 읽지 못할 경우 발생
     */
    public static byte[] getPage(String filePath) throws IOException {
        File file = new File(filePath);

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] body = new byte[(int) file.length()];
            fis.read(body);
            return body;
        } catch (IOException e) {
            throw new IOException("cannot find file.");
        }
    }
}
