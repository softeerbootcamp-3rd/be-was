package http.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Parser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MultiPartForm {
    private static final Logger logger = LoggerFactory.getLogger(MultiPartForm.class);
    private static final String FILE_PATH = "./src/main/resources/data/";
    private Map<String, String> fields; // 텍스트 형식의 필드 데이터 관리

    public MultiPartForm(BufferedReader br, String contentType) throws IOException {
        this.fields = new HashMap<>();

        // 멀티파트 폼 데이터 의 바운더리 추출 (멀티파트 데이터의 각 부분을 구분하는 구분자)
        String boundary = Parser.extractBoundary(contentType);
        // 바운더리를 이용하여 바디 파싱
        processMultipartData(br, boundary);
    }

    private void processMultipartData(BufferedReader br, String boundary) throws IOException {
        // 라인 단위로 데이터 읽기
        String line;
        while ((line = br.readLine()) != null) {
            // 바운더리 확인
            if (line.equals("--" + boundary)) {
                // 멀티파트 데이터 파트 시작
                processMultipart(br);
            } else if (line.equals("--" + boundary + "--")) {
                // 해당 파트의 끝을 나타내는 구분선을 만날 경우 종료
                break;
            }
        }
    }

    private void processMultipart(BufferedReader br) throws IOException {
        // 멀티파트 파트 처리 로직
        String fieldName = null;
        String fileName = null;
        StringBuilder fieldValue = new StringBuilder();
        ByteArrayOutputStream fileValue = new ByteArrayOutputStream();

        boolean isFilePart = false;
        while (true) {
            String line = br.readLine();
            logger.debug(line);

            if (line == null) {
                break;
            }

            if (line.startsWith("Content-Disposition:")) {
                isFilePart = false;
                // Content-Disposition 헤더에서 필드명과 파일명 추출
                fieldName = Parser.extractFieldName(line);
                fileName = Parser.extractFileName(line);
                if (!fileName.isEmpty()) {
                    // 파일 이름에 띄어쓰기가 있으면 '_'로 대체
                    fileName = fileName.replace(" ", "_");
                    isFilePart = true;
                }
            } else if (line.startsWith("Content-Type:") && isFilePart) {
                // Content-Type 헤더 다음부터 파일 데이터 시작
                // 첫 줄 빈 라인 비우기
                br.readLine();
                readFileData(br, fileValue);
                break;
            } else if (line.isEmpty()) {
                // 빈 줄을 만나면 다음부터는 파트의 본문이므로 해당 데이터를 저장하고 종료
                fieldValue.append(br.readLine());
                break;
            }
        }

        // 디버깅을 위한 로그 추가
        logger.debug("fieldName: " + fieldName);
        logger.debug("fileName: " + fileName);
        logger.debug("fieldValue: " + fieldValue);

        // 필드가 존재하면 필드 추가
        if (fieldName != null && !fieldName.isEmpty()) {
            fields.put(fieldName, fieldValue.toString());
        }

        if (fileName != null && !fileName.isEmpty()) {
            byte[] fileBytes = fileValue.toByteArray();
            fields.put("file", fileName);

            // 파일을 저장할 경로 설정 (resources/data/file 폴더에 저장되도록 설정)
            saveFile(fileName, fileBytes);
        }
    }

    private void readFileData(BufferedReader br, ByteArrayOutputStream fieldValue) throws IOException {
        // %%EOF을 만날 때까지 계속 읽어와서 fieldValue에 저장
        while (true) {
            String line = br.readLine();
            if (line == null || line.contains("%%EOF") || line.isEmpty()) {
                // EOF이거나 빈 라인이 들어올 경우 종료
                break;
            }
            fieldValue.write(line.getBytes(StandardCharsets.UTF_8));
        }
    }

    private void saveFile(String fileName, byte[] fileBytes) {
        // 파일 저장
        try (FileOutputStream fos = new FileOutputStream(FILE_PATH + fileName)) {
            fos.write(fileBytes);
        } catch (IOException e) {
            logger.error("Failed to save file: " + fileName, e);
        }
    }

    public Map<String, String> getFields() {
        return fields;
    }
}