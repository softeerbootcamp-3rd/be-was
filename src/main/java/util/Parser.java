package util;

import annotation.Column;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class Parser {
    public static <T> T jsonParser(Class<T> clazz, String body) throws Exception {
        Constructor<T> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);

        T object = constructor.newInstance();
        Field[] fields = clazz.getDeclaredFields();

        Map<String, String> keyValueMap = parseJson(body);
        setField(object, fields, keyValueMap);

        return object;
    }

    public static <T> T jsonParser(Class<T> clazz, byte[] body) throws Exception {
        Constructor<T> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);

        T object = constructor.newInstance();
        Field[] fields = clazz.getDeclaredFields();

        Map<String, String> keyValueMap = parseMultipart(body);
        setField(object, fields, keyValueMap);

        return object;
    }

    private static Map<String, String> parseJson(String body) throws Exception {

        String[] jsons = body.split("&");

        Map<String, String> keyValueMap = new HashMap<>();

        for (String s : jsons) {
            String[] keyValue = s.split("=", -1);

            if (keyValue[1].isEmpty()) throw new Exception("value가 유효하지 않습니다.");

            keyValueMap.put(keyValue[0], keyValue[1]);
        }

        return keyValueMap;
    }

    private static Map<String, String> parseMultipart(byte[] bodys) throws Exception {
        String body = new String(bodys, "ISO-8859-1");
        Map<String, String> keyValueMap = new HashMap<>();

        String boundary = body.split("\r\n")[0]; // 바운더리 추출
        String[] parts = body.split(boundary); // 바운더리를 기준으로 파트 분리

        for (String part : parts) {
            part = part.trim(); // 앞뒤 공백 제거
            if (part.isEmpty()) continue;

            String[] headersAndBody = part.split("\r\n\r\n", 2); // 실제 데이터와 헤더 분리
            if (headersAndBody.length < 2) continue;

            String headers = headersAndBody[0]; // 예시: Content-Disposition: form-data; name="content"
            String bodyContent = headersAndBody[1].trim(); // 예시: Hello World

            String[] headerLines = headers.split("\r\n");
            extract(part, headerLines, keyValueMap, bodyContent);
        }

        return keyValueMap;
    }

    private static void extract(String part, String[] headerLines, Map<String, String> keyValueMap, String bodyContent) throws Exception {
        for (String headerLine : headerLines) {
            if (headerLine.startsWith("Content-Disposition")) {
                String fieldName = headerLine.split("name=\"")[1].split("\"")[0]; // name="content"에서 content 추출

                if (fieldName.equals("fileUpload")) {
                    int indexOfBinaryStart = part.indexOf("\r\n\r\n") + 4;

                    if (indexOfBinaryStart >= 0 && indexOfBinaryStart < part.length()) {
                        String staticDirPath = "src/main/resources/static/"; // 파일을 저장할 경로
                        String outputFileName = "test.jpeg"; // 저장할 파일 이름
                        String outputPath = staticDirPath + outputFileName; // 최종 파일 경로

                        String binaryDataString = part.substring(indexOfBinaryStart);
                        byte[] binaryData = binaryDataString.getBytes("ISO-8859-1");

                        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                            fos.write(binaryData); // 바이너리 데이터를 파일로 저장
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        keyValueMap.put(fieldName, "/" + outputFileName); // 파일 이름을 맵에 저장
                    }
                } else {
                    keyValueMap.put(fieldName, bodyContent); // 텍스트 필드 값을 맵에 저장
                }
            }
        }
    }


    private static void setField(Object object, Field[] fields, Map<String, String> keyValueMap) throws Exception {
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                String value = keyValueMap.get(field.getName());

                if (value == null) throw new Exception("필드명이 유효하지 않습니다.");

                field.setAccessible(true);
                field.set(object, value);
            }
        }
    }
}
