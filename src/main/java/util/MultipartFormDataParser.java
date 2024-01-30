package util;

import constant.HttpHeader;
import dto.HttpRequestDto;
import dto.PostDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MultipartFormDataParser {
    private static final Logger logger = LoggerFactory.getLogger(MultipartFormDataParser.class);

    // multipart/form-data 형식의 HTTP Request를 파싱해서 PostDto 객체를 만들어 반환
    public static PostDto parseMultipartFormData(HttpRequestDto request) {
        PostDto postDto = new PostDto();
        // parsing boundary
        String[] contentType = request.getHeaders().get(HttpHeader.CONTENT_TYPE).split("; ");
        Map<String, String> contentTypeValue = new HashMap<>();
        for (String value : contentType) {
            String[] keyValue = value.split("=");
            if (keyValue.length == 2) {
                contentTypeValue.put(keyValue[0], keyValue[1]);
            }
        }
        String boundary = "--" + contentTypeValue.get("boundary");
        // 파싱한 boundary 문자열을 바이트 배열로 변환
        byte[] boundaryBytes = boundary.getBytes(StandardCharsets.UTF_8);

        // HttpRequestDto의 body 가져오기
        byte[] body = request.getBodyByte();

        // body에서 boundary 문자열이 나타나는 첫 번째 인덱스 구하기
        int index = indexOf(body, boundaryBytes, 0);

        while (index != -1) {
            // 찾은 boundary 다음부터 데이터 파싱
            index += boundaryBytes.length + 2; // boundary 뒤로 이동 + \r\n

            // 헤더가 시작하고 끝나는 인덱스 찾기 (\r\n\r\n으로 구분)
            int headerStart = index;
            int headerEnd = indexOf(body, "\r\n\r\n".getBytes(StandardCharsets.UTF_8), headerStart);

            if (headerEnd != -1) {
                // 헤더 내용 추출
                byte[] headerBytes = Arrays.copyOfRange(body, headerStart, headerEnd);
                String header = new String(headerBytes, StandardCharsets.UTF_8);

                // 헤더에서 필요한 정보 파싱하기
                Map<String, String> parameters = new HashMap<>();
                extractValueFromHeader(header, parameters);

                // 한 줄 띄고 value 정보 파싱
                int valueStart = headerEnd + 4; // \r\n\r\n
                int nextBoundaryIndex = indexOf(body, boundaryBytes, index);

                if (nextBoundaryIndex != -1) {
                    byte[] fileData = Arrays.copyOfRange(body, valueStart, nextBoundaryIndex - 2);
                    String value = new String(fileData, StandardCharsets.UTF_8);

                    if (parameters.get("name").equals("file") && !value.isEmpty()) {
                        // name이 file인 경우: byte[]를 그대로 저장
                        postDto.setFile(fileData);
                        postDto.setFileName(parameters.get("filename").replace(" ", ""));
                        postDto.setFileContentType(parameters.get("Content-Type"));
                    } else if (parameters.get("name").equals("title")) {
                        // name이 title인 경우: String으로 변환해서 저장
                        postDto.setTitle(value);
                    } else if (parameters.get("name").equals("contents")) {
                        // name이 contents인 경우: String으로 변환해서 저장
                        postDto.setContents(value);
                    }

                    index = nextBoundaryIndex;
                } else {
                    // 더 이상 boundary가 없으면 종료
                    break;
                }
            } else {
                // 헤더가 없으면 종료
                break;
            }
        }
        return postDto;
    }

    // source 배열에서 fromIndex 위치부터 탐색해서 target 배열과 일치하는 부분이 있으면 시작하는 위치 인덱스 반환
    private static int indexOf(byte[] source, byte[] target, int fromIndex) {
        System.out.println("index Of > sourceLength: " + source.length + " target.length: " + target.length + " fromIndex: " + fromIndex);
        for (int i = fromIndex; i <= source.length - target.length; i++) {
            boolean found = true;
            for (int j = 0; j < target.length; j++) {
                if (source[i + j] != target[j]) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return i;
            }
        }
        return -1;
    }

    // 헤더를 한 줄씩 읽으며 필요한 정보 파싱해서 Map에 저장
    private static void extractValueFromHeader(String headers, Map<String, String> parameters) {
        BufferedReader reader = new BufferedReader(new StringReader(headers));

        while (true) {
            String line = null;
            try {
                line = reader.readLine();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
            // 더 이상 읽을 문자열이 없으면 종료
            if (line == "" || line == null)
                break;

            String[] header = line.split(": ");
            String headerName = header[0];

            if (headerName.equals("Content-Type")) {
                parameters.put("Content-Type", header[1]);
            } else {
            String[] pairs = header[1].split("; ");

            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    parameters.put(keyValue[0], keyValue[1].replace("\"", ""));
                }
            }
            }
        }
    }
}
